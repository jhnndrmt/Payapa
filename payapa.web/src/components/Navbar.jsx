import React, { useState, useEffect } from "react";
import {
  Navbar,
  Container,
  Nav,
  OverlayTrigger,
  Popover,
} from "react-bootstrap";
import AccountCircle from "@mui/icons-material/AccountCircle";
import "../index.css";

function NavigationBar() {
  const [isMobile, setIsMobile] = useState(false);

  useEffect(() => {
    const checkMobile = () => {
      const mobile = window.innerWidth <= 768;
      setIsMobile(mobile);
      console.log("Is mobile:", mobile);
    };

    checkMobile();
    window.addEventListener("resize", checkMobile);

    return () => window.removeEventListener("resize", checkMobile);
  }, []);

  const popover = (
    <Popover id="popover-basic">
      <Popover.Header as="h3">User Menu</Popover.Header>
      <Popover.Body>
        <Nav.Link href="#profile">Profile</Nav.Link>
        <Nav.Link href="#settings">Settings</Nav.Link>
        <Nav.Link href="#logout">Logout</Nav.Link>
      </Popover.Body>
    </Popover>
  );

  return (
    <Navbar expand="lg" className="bg-body-tertiary">
      <Container className="d-flex justify-content-between align-items-center">
        <Navbar.Brand href="#home">
          <b>Payapa.</b>
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="mx-auto breadcrumb-nav d-flex align-items-center">
            <Nav.Link href="#home" active>
              Dashboard
            </Nav.Link>
            <Nav.Link href="#features">Students</Nav.Link>
            <Nav.Link href="#pricing">Appointment</Nav.Link>
          </Nav>
          <OverlayTrigger trigger="click" placement="bottom" overlay={popover}>
            <Navbar.Text className="logout-mobile">
              {isMobile ? (
                <span>
                  <b className="text-danger">Logout</b>
                </span>
              ) : (
                <AccountCircle sx={{ width: "30px", height: "30px" }} />
              )}
            </Navbar.Text>
          </OverlayTrigger>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default NavigationBar;
