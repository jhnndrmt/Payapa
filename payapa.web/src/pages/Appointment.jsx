import React from "react";
import "../index.css";

import {
  Container,
  Breadcrumb,
  Row,
  Col,
  Card,
  Spinner,
} from "react-bootstrap";

import useAppointments from "../hooks/useAppointments";

function Appointments() {
  const { appointments = [], loading, error } = useAppointments();

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
                    <Card key={appointment.id}>
                      <Card.Body>
                        {appointment.user ? (
                          <>
                            <p>
                              {appointment.user.firstName}
                              {appointment.user.lastName}
                            </p>
                            <p>{appointment.user.email}</p>
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
                    <Card.Body>Body 2</Card.Body>
                  </Card>
                </div>
                <Row className="mt-3">
                  <Col>
                    <Card>
                      <Card.Body>Body 3</Card.Body>
                    </Card>
                  </Col>
                  <Col>
                    <Card>
                      <Card.Body>Body 4</Card.Body>
                    </Card>
                  </Col>
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
