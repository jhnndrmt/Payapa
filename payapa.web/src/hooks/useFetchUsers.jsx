import { useState, useEffect } from "react";
import { firestore } from "../services/Firebase";
import { collection, getDocs } from "firebase/firestore";

function useFetchUsers() {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const querySnapshot = await getDocs(collection(firestore, "users"));
        const usersList = querySnapshot.docs.map((doc) => ({
          id: doc.id,
          ...doc.data(),
        }));
        setUsers(usersList);
      } catch (err) {
        setError(err);
        console.error("Error fetching users:", err);
      }
    };

    fetchUsers();
  }, []);

  return { users, error };
}

export default useFetchUsers;
