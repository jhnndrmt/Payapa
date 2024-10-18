import React from "react";
import "../index.css";

import { Container, Breadcrumb, Card, Table } from "react-bootstrap";
import useFetchUsers from "../hooks/useFetchUsers";

function Student() {
  const { users } = useFetchUsers();
  return (
    <>
      <Container className="mt-5">
        <Breadcrumb>
          <Breadcrumb.Item href="#" active>
            Student
          </Breadcrumb.Item>
        </Breadcrumb>

        <div>
          <Card>
            <Card.Header>Students list</Card.Header>
            <Card.Body>
              <Table responsive bordered hover>
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>ID</th>
                    <th>Course</th>
                    <th>Status</th>
                    <th>Ave Status</th>
                  </tr>
                </thead>
                <tbody>
                  {users.map((user) => (
                    <tr key={user.id}>
                      <td>
                        {`${user.firstName}`} {`${user.lastName}`}
                      </td>
                      <td>{`${user.studentID}`}</td>
                      <td>{`${user.course}`}</td>
                      <td>Status</td>
                      <td>Ave Status</td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            </Card.Body>
          </Card>
        </div>
      </Container>
    </>
  );
}

export default Student;
