import React, { useState } from "react";
import "../index.css";

import { Container, Breadcrumb, Card, Table, Dropdown } from "react-bootstrap";
import useFetchUsers from "../hooks/useFetchUsers";
import FilterDropdown from "../components/CourseFilter";

function Student() {
  const { users } = useFetchUsers();
  const [selectedCourse, setSelectedCourse] = useState("");

  const handleFilterSelect = (course) => {
    setSelectedCourse(course);
  };

  const filteredUsers = selectedCourse
    ? users.filter((user) => user.course === selectedCourse)
    : users;

  return (
    <>
      <Container className="mt-5">
        <Breadcrumb className="cursor-pointer">
          <Breadcrumb.Item active>Student</Breadcrumb.Item>
        </Breadcrumb>

        <div>
          <Card>
            <Card.Header>Students list</Card.Header>
            <Card.Body>
              <Table className="cursor-pointer" bordered hover>
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>ID</th>
                    <th className="d-flex justify-content-between">
                      Course
                      <FilterDropdown onSelect={handleFilterSelect} />
                    </th>
                    <th>Status</th>
                    <th>Ave Status</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredUsers.map((user) => (
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
