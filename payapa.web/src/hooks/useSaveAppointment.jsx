import { useState } from "react";
import { firestore } from "../services/Firebase";
import { doc, setDoc, getDoc } from "firebase/firestore";

const useSaveAppointment = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const saveAppointment = async (appointmentData) => {
    setLoading(true);
    setError(null);

    try {
      // Fetch user data based on userId to get the name
      const userDocRef = doc(firestore, "users", appointmentData.userId);
      const userDoc = await getDoc(userDocRef);

      if (!userDoc.exists()) {
        throw new Error("User not found");
      }

      const userData = userDoc.data();
      const fullName = `${userData.firstName} ${userData.lastName}`;

      const appointmentDocRef = doc(
        firestore,
        "scheduledAppointments",
        appointmentData.userId
      );

      await setDoc(
        appointmentDocRef,
        {
          name: fullName,  // Add fetched name here
          date: appointmentData.date,
          time: appointmentData.time,
          message: appointmentData.message,
        },
        { merge: true }
      );

      return true;
    } catch (err) {
      setError(err);
      return false;
    } finally {
      setLoading(false);
    }
  };

  return { saveAppointment, loading, error };
};

export default useSaveAppointment;
