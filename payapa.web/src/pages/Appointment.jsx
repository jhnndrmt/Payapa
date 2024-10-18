import React from "react";
import "../index.css";

import { Container, Breadcrumb } from "react-bootstrap";

function Appointments() {
  return (
    <>
      <Container className="mt-5">
        <Breadcrumb>
          <Breadcrumb.Item href="#" active>
            Appointments
          </Breadcrumb.Item>
        </Breadcrumb>
      </Container>
    </>
  );
}

export default Appointments;
