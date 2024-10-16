import React from "react";
import { Routes, Route } from "react-router-dom";
import NavigationBar from "../components/NavBar";
import Dashboard from "./Dashboard";
import Students from "./Students";
import "../index.css";

function MainPage() {
  return (
    <div>
      <NavigationBar />
      <div className="content">
        <Routes>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/student" element={<Students />} />
          {/* Add more routes as needed */}
        </Routes>
      </div>
    </div>
  );
}

export default MainPage;
