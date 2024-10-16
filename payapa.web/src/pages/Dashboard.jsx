import React from "react";
import "../index.css";

import { Container, Breadcrumb } from "react-bootstrap";

function Dashboard() {
  return (
    <>
      <Container className="mt-5">
        <Breadcrumb>
          <Breadcrumb.Item href="#" active>
            Dashboard
          </Breadcrumb.Item>
        </Breadcrumb>
      </Container>
    </>
  );
}

export default Dashboard;
