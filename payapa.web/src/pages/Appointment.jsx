import React, { useState } from "react";
import "../index.css";

import {
  Container,
  Breadcrumb,
  Row,
  Col,
  Card,
  Spinner,
  Button,
  Form,
} from "react-bootstrap";

import useAppointments from "../hooks/useAppointments";
import AddIcon from "@mui/icons-material/Add";

function Appointments() {
  const { appointments = [], loading, error } = useAppointments();
  const [selectedUser, setSelectedUser] = useState(null);
  const [showAppointmentForm, setShowAppointmentForm] = useState(false);

  const handleUserClick = (appointment) => {
    setSelectedUser(appointment.user);
    console.log("Selected User:", appointment.user);
  };

  const handleSetAppointmentClick = () => {
    setShowAppointmentForm(!showAppointmentForm);
  };

  return (
    <>
      <Container className="mt-5">
        <Breadcrumb>
          <Breadcrumb.Item href="#" active>
            Appointments
          </Breadcrumb.Item>
        </Breadcrumb>

        {loading && <Spinner animation="border" />}

        <div>
          {!loading && !error && (
            <Row>
              <Col sm={4}>
                {appointments
                  .filter((appointment) => appointment.user)
                  .map((appointment) => (
                    <Card
                      key={appointment.id}
                      className="mb-3 cursor-pointer"
                      onClick={() => handleUserClick(appointment)}
                    >
                      <Card.Body>
                        {appointment.user ? (
                          <>
                            <h4>{`${appointment.user.firstName} ${appointment.user.lastName}`}</h4>
                            <p className="text-muted">
                              {appointment.user.email}
                            </p>
                          </>
                        ) : (
                          <p>No user found for this appointment</p>
                        )}
                      </Card.Body>
                    </Card>
                  ))}
              </Col>

              <Col sm={8}>
                <div>
                  <Card>
                    <Card.Body>
                      {selectedUser ? (
                        <>
                          <div className="d-flex justify-content-between align-items-center">
                            <h4 className="mb-0">{`${selectedUser.firstName} ${selectedUser.lastName}`}</h4>
                            <div>
                              <Button
                                variant="success"
                                className="me-2 d-flex justify-content-between align-items-center"
                                size="sm"
                                onClick={handleSetAppointmentClick}
                              >
                                <AddIcon className="me-2" /> Set Appointment
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
                <Row className="mt-3">
                  <Col>
                    <Card>
                      <Card.Header>
                        <div className="d-flex justify-content-between align-items-center">
                          <strong>Message</strong>
                        </div>
                      </Card.Header>
                      <Card.Body>
                        {selectedUser && selectedUser.reasonForStress ? (
                          <textarea
                            value={selectedUser.reasonForStress}
                            disabled
                            style={{ width: "100%" }}
                          />
                        ) : (
                          <p>No reason for stress provided</p>
                        )}
                      </Card.Body>
                    </Card>
                  </Col>
                  {showAppointmentForm && (
                    <Col>
                      <Card>
                        <Card.Body>
                          <Form>
                            <Form.Group controlId="appointmentDate">
                              <Form.Label>Appointment Date</Form.Label>
                              <Form.Control type="date" />
                            </Form.Group>
                            <Form.Group
                              controlId="appointmentTime"
                              className="mt-3"
                            >
                              <Form.Label>Appointment Time</Form.Label>
                              <Form.Control type="time" />
                            </Form.Group>
                            <Form.Group
                              controlId="appointmentMessage"
                              className="mt-3"
                            >
                              <Form.Label>Appointment Message</Form.Label>
                              <Form.Control as="textarea" />
                            </Form.Group>
                            <div className="d-flex justify-content-end">
                              <Button
                                variant="primary"
                                className="mt-3"
                                size="sm"
                              >
                                Confirm Appointment
                              </Button>
                            </div>
                          </Form>
                        </Card.Body>
                      </Card>
                    </Col>
                  )}
                </Row>
              </Col>
            </Row>
          )}
        </div>
      </Container>
    </>
  );
}

export default Appointments;
