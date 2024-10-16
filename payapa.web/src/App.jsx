import React from "react";
import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Toastify from "./components/Toastify";
import { UserProvider } from "./services/UserContext";

import Login from "./pages/Login";
import Main from "./pages/MainPage";

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
        <div className="content">
          <UserProvider>
            <Router>
              <Toastify />
              <Routes>
                <Route path="/" element={<Login />} />
                <Route path="/main/*" element={<Main />} />
              </Routes>
            </Router>
          </UserProvider>
        </div>
      </main>
    </>
  );
}

export default App;
