// Copyright (c) 2020 Facebook, Inc. and its affiliates.
// All rights reserved.
//
// This source code is licensed under the BSD-style license found in the
// LICENSE file in the root directory of this source tree.

package com.example.payapa;

import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

class Result {
    int classIndex;
    Float score;
    Rect rect;

    public Result(int cls, Float output, Rect rect) {
        this.classIndex = cls;
        this.score = output;
        this.rect = rect;
    }
};

public class PrePostProcessor {
    // for yolov5 model, no need to apply MEAN and STD
    static float[] NO_MEAN_RGB = new float[] {0.0f, 0.0f, 0.0f};
    static float[] NO_STD_RGB = new float[] {1.0f, 1.0f, 1.0f};

    // model input image size
    static int mInputWidth = 640;
    static int mInputHeight = 640;

    // model output is of size 25200*(num_of_class+5)
    //private static int mOutputRow = 25200; // as decided by the YOLOv5 model for input image of size 640*640
    //private static int mOutputRow = 6300; // as decided by the YOLOv5 model for input image of size 320*320
    static int mOutputRow = 25200;
    private static int mOutputColumn = 7 + 5; // left, top, right, bottom, score and 80 class probability
    private static float mThreshold = 0.5f; // score above which a detection is generated
    private static int mNmsLimit = 5;

    static String[] mClasses;


    /**
     Removes bounding boxes that overlap too much with other boxes that have
     a higher score.
     - Parameters:
     - boxes: an array of bounding boxes and their scores
     - limit: the maximum number of boxes that will be selected
     - threshold: used to decide whether boxes overlap too much
     */
    static ArrayList<Result> nonMaxSuppression(ArrayList<Result> boxes, int limit, float threshold) {

        // Do an argsort on the confidence scores, from high to low.
        Collections.sort(boxes,
                new Comparator<Result>() {
                    @Override
                    public int compare(Result o1, Result o2) {
                        return o2.score.compareTo(o1.score);
                    }
                });

        ArrayList<Result> selected = new ArrayList<>();
        boolean[] active = new boolean[boxes.size()];
        Arrays.fill(active, true);
        int numActive = active.length;

        // The algorithm is simple: Start with the box that has the highest score.
        // Remove any remaining boxes that overlap it more than the given threshold
        // amount. If there are any boxes left (i.e. these did not overlap with any
        // previous boxes), then repeat this procedure, until no more boxes remain
        // or the limit has been reached.
        boolean done = false;
        for (int i=0; i<boxes.size() && !done; i++) {
            if (active[i]) {
                Result boxA = boxes.get(i);
                selected.add(boxA);

                if (selected.size() >= limit) break;

                for (int j=i+1; j<boxes.size(); j++) {
                    if (active[j]) {
                        Result boxB = boxes.get(j);
                        if (IOU(boxA.rect, boxB.rect) > threshold ) {
                            active[j] = false;
                            numActive -= 1;
                            if (numActive <= 0) {
                                done = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        HashSet<Integer> classDetected = new HashSet<>();
        for (Result box : boxes) {
            boolean isAlreadySelected = false;
            for (Result boxSelected : selected) {
                if (boxSelected.classIndex == box.classIndex) {
                    isAlreadySelected = true;
                    break;
                }
            }
            if (!isAlreadySelected) {
                selected.add(box);
            }
        }

        return selected;
    }

    /**
     Computes intersection-over-union overlap between two bounding boxes.
     */
    static float IOU(Rect a, Rect b) {
        float areaA = (a.right - a.left) * (a.bottom - a.top);
        if (areaA <= 0.0) return 0.0f;

        float areaB = (b.right - b.left) * (b.bottom - b.top);
        if (areaB <= 0.0) return 0.0f;

        float intersectionMinX = Math.max(a.left, b.left);
        float intersectionMinY = Math.max(a.top, b.top);
        float intersectionMaxX = Math.min(a.right, b.right);
        float intersectionMaxY = Math.min(a.bottom, b.bottom);
        float intersectionArea = Math.max(intersectionMaxY - intersectionMinY, 0) *
                Math.max(intersectionMaxX - intersectionMinX, 0);
        return intersectionArea / (areaA + areaB - intersectionArea);
    }

    static ArrayList<Result> outputsToNMSPredictions(float[] outputs, float imgScaleX, float imgScaleY, float ivScaleX, float ivScaleY, float startX, float startY) {
        List<Result> results = new ArrayList<>();
        Log.i("Detection", "Start detection...");
        HashSet<Integer> classFound = new HashSet<>();
        //Log.wtf("WTF", String.valueOf(outputs.length));
        //Log.wtf("WTF", String.valueOf(mOutputRow));
        // outputs.length == mOutputRow*mOutputColumn + 4
        for (int i = 0; i< mOutputRow; i++) {
            if (outputs[i* mOutputColumn +4] > mThreshold) {
                float x = outputs[i * mOutputColumn];
                float y = outputs[i * mOutputColumn + 1];
                float w = outputs[i * mOutputColumn + 2];
                float h = outputs[i * mOutputColumn + 3];

                float left = imgScaleX * (x - w / 2);
                float top = imgScaleY * (y - h / 2);
                float right = imgScaleX * (x + w / 2);
                float bottom = imgScaleY * (y + h / 2);

                float max = outputs[i * mOutputColumn + 4]; //score
                int cls = 0;
                //Log.wtf("WTF", "**************************************");
                for (int j = 0; j < mOutputColumn - 5; j++) {
                    if (outputs[i * mOutputColumn + 5 + j] > max) {
                        max = outputs[i * mOutputColumn + 5 + j];
                        //Log.wtf("AA", String.valueOf(cls));
                        cls = j;
                        //Log.wtf("OK", String.valueOf(j) + " " + String.valueOf(cls));
                    }
                }
                classFound.add(cls);
                Rect rect = new Rect((int) (startX + ivScaleX * left), (int) (startY + top * ivScaleY), (int) (startX + ivScaleX * right), (int) (startY + ivScaleY * bottom));
                Result result = new Result(cls, outputs[i * mOutputColumn + 4], rect);
                results.add(result);

            }
        }
        Collections.sort(results, (o1, o2) -> o2.score.compareTo(o1.score));

        if (!results.isEmpty()) {
            results = results.subList(0, 1);
        }

        Log.i("Detection", "Result found:" + Integer.toString(results.size()));
        for (int cls : classFound) {
            Log.i("Detection", mClasses[cls]);
        }
        for (Result result : results) {
            Log.i("Match:", mClasses[result.classIndex] + " " + String.valueOf(result.score) );
        }
        return nonMaxSuppression(new ArrayList<>(results), mNmsLimit, mThreshold);
    }
}
