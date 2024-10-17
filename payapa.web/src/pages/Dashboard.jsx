import { useEffect, useState } from "react";
import "../index.css";
import {
  Container,
  Breadcrumb,
  Card,
  Row,
  Col,
  Button,
  Spinner,
} from "react-bootstrap";

import { toast } from "react-toastify";
import { firestore } from "../services/Firebase";
import {
  collection,
  getDocs,
  doc,
  updateDoc,
  deleteDoc,
} from "firebase/firestore";

function Dashboard() {
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [isImageLoading, setIsImageLoading] = useState(true);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const querySnapshot = await getDocs(collection(firestore, "users"));
        const usersList = querySnapshot.docs.map((doc) => ({
          id: doc.id,
          ...doc.data(),
        }));
        setUsers(usersList);
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    };

    fetchUsers();
  }, []);

  const handleUserClick = (user) => {
    setSelectedUser(user);
  };

  const handleApprove = async () => {
    if (selectedUser) {
      const userRef = doc(firestore, "users", selectedUser.id);
      await updateDoc(userRef, { isAccountValidated: true });
      toast.success("User approved!");
    }
  };

  const handleDecline = async () => {
    if (selectedUser) {
      const userRef = doc(firestore, "users", selectedUser.id);
      await deleteDoc(userRef);
      toast.error("User declined and removed from the database.");

      setUsers((prevUsers) =>
        prevUsers.filter((user) => user.id !== selectedUser.id)
      );

      setSelectedUser(null);
    }
  };

  return (
    <>
      <Container className="mt-5">
        <Breadcrumb>
          <Breadcrumb.Item href="#" active>
            Dashboard
          </Breadcrumb.Item>
        </Breadcrumb>

        <div>
          <Row style={{ height: "800px" }}>
            <Col sm={4} style={{ maxHeight: "100%", overflowY: "auto" }}>
              {users.map((user) => (
                <Card
                  key={user.id}
                  className="mb-3"
                  onClick={() => handleUserClick(user)}
                  style={{ cursor: "pointer" }}
                >
                  <Card.Body>
                    <h4>{`${user.firstName} ${user.lastName}`}</h4>
                    <p className="text-muted">{user.email}</p>
                  </Card.Body>
                </Card>
              ))}
            </Col>

            <Col sm={8}>
              <Row>
                <div className="mb-3">
                  <Card>
                    <Card.Body>
                      {selectedUser ? (
                        <>
                          <div className="d-flex justify-content-between align-items-center">
                            <h4 className="mb-0">{`${selectedUser.firstName} ${selectedUser.lastName}`}</h4>
                            <div>
                              <Button
                                variant="success"
                                className="me-2"
                                onClick={handleApprove}
                              >
                                Approve
                              </Button>
                              <Button variant="danger" onClick={handleDecline}>
                                Decline
                              </Button>
                            </div>
                          </div>
                        </>
                      ) : (
                        <p>Select a student to view details</p>
                      )}
                    </Card.Body>
                  </Card>
                </div>

                {selectedUser && (
                  <div>
                    <Card>
                      <Card.Body>
                        <Row>
                          <Col
                            sm={4}
                            className="d-flex justify-content-center align-items-center"
                          >
                            {isImageLoading && (
                              <Spinner animation="border" role="status">
                                <span className="visually-hidden">
                                  Loading...
                                </span>
                              </Spinner>
                            )}
                            <img
                              src={selectedUser.photoUrl}
                              alt={`${selectedUser.firstName} ${selectedUser.lastName}`}
                              style={{
                                width: "100%",
                                height: "100%",
                                objectFit: "cover",
                                borderRadius: "8px",
                              }}
                              onLoad={() => setIsImageLoading(false)}
                            />
                          </Col>

                          <Col sm={8}>
                            <p>
                              <strong>Age:</strong> {selectedUser.age}
                            </p>
                            <p>
                              <strong>Course:</strong> {selectedUser.course}
                            </p>
                            <p>
                              <strong>Email:</strong> {selectedUser.email}
                            </p>
                            <p>
                              <strong>Gender:</strong> {selectedUser.gender}
                            </p>
                            <p>
                              <strong>Nickname:</strong> {selectedUser.nickname}
                            </p>
                            <p>
                              <strong>Student ID:</strong>
                              {selectedUser.studentID}
                            </p>
                          </Col>
                        </Row>
                      </Card.Body>
                    </Card>
                  </div>
                )}
              </Row>
            </Col>
          </Row>
        </div>
      </Container>
    </>
  );
}

export default Dashboard;
