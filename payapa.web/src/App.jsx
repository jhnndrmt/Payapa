import React from "react";
import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Toastify from "./components/Toastify";
import { UserProvider } from "./services/UserContext";

import Login from "./pages/Login";
import Main from "./pages/MainPage";
import Dashboard from "./pages/Dashboard";

function App() {
  return (
    <>
      <main
        className="login-bg"
        style={{
          display: "flex",
          flexDirection: "column",
          minHeight: "100vh",
          fontFamily: "Colon Mono, monospace",
        }}
      >
        <UserProvider>
          <Router>
            <Toastify />
            <Routes>
              {/* Enter Route here */}
              <Route path="/" element={<Login />} />
              <Route path="/main" element={<Main />} />
              <Route path="/dashboard" element={<Dashboard />} />
            </Routes>
          </Router>
        </UserProvider>
      </main>
    </>
  );
}

export default App;
