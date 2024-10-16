import React from "react";
import "../index.css";

import { Container, Breadcrumb } from "react-bootstrap";

function Student() {
  return (
    <>
      <Container className="mt-5">
        <Breadcrumb>
          <Breadcrumb.Item href="#" active>
            Student
          </Breadcrumb.Item>
        </Breadcrumb>
      </Container>
    </>
  );
}

export default Student;
