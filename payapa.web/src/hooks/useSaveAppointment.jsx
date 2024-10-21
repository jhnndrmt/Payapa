import { useState } from "react";
import { firestore } from "../services/Firebase";
import { collection, addDoc } from "firebase/firestore";

const useSaveAppointment = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const saveAppointment = async (appointmentData) => {
    setLoading(true);
    setError(null);

    try {
      const appointmentsCollection = collection(
        firestore,
        "scheduledAppointments"
      );
      await addDoc(appointmentsCollection, appointmentData);
      return true; // Appointment saved successfully
    } catch (err) {
      setError(err);
      return false; // Error occurred while saving
    } finally {
      setLoading(false);
    }
  };

  return { saveAppointment, loading, error };
};

export default useSaveAppointment;
