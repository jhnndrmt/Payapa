import React from "react";
import "./App.css";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  useLocation,
} from "react-router-dom";
import Toastify from "./components/Toastify";
import { UserProvider } from "./services/UserContext";

import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Student from "./pages/Students";
import NavigationBar from "./components/NavBar";

// Create a wrapper component to conditionally render the NavigationBar
function AppContent() {
  const location = useLocation();

  // Hide the NavigationBar on the login page
  const hideNavBar = location.pathname === "/";

  return (
    <>
      {/* Conditionally render the NavigationBar */}
      {!hideNavBar && <NavigationBar />}
      <Toastify />
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/student" element={<Student />} />
      </Routes>
    </>
  );
}

function App() {
  return (
    <main
      className="login-bg"
      style={{
        display: "flex",
        flexDirection: "column",
        minHeight: "100vh",
        fontFamily: "Colon Mono, monospace",
      }}
    >
      <div className="content">
        <UserProvider>
          <Router>
            <AppContent /> {/* AppContent wrapped within the Router */}
          </Router>
        </UserProvider>
      </div>
    </main>
  );
}

export default App;
