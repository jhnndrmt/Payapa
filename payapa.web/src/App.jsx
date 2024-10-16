import React from "react";
import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Toastify from "./components/Toastify";

import Login from "./pages/Login";
import Main from "./pages/MainPage";
import Dashboard from "./pages/Dashboard";

function App() {
  return (
    <>
      <main
        style={{
          display: "flex",
          flexDirection: "column",
          minHeight: "100vh",
          fontFamily: "Colon Mono, monospace",
          backgroundColor: "#ECE3CE",
        }}
      >
        <Router>
          <Toastify />
          <Routes>
            {/* Enter Route here */}
            <Route path="/" element={<Login />} />
            <Route path="/main" element={<Main />} />
            <Route path="/dashboard" element={<Dashboard />} />
          </Routes>
        </Router>
      </main>
    </>
  );
}

export default App;
