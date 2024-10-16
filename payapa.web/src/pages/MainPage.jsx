import React from "react";
import NavigationBar from "../components/NavBar";
import "../index.css";

function MainPage() {
  return (
    <>
      <div className="login-bg">
        <NavigationBar />
        <div>
          <p className="text-dark"> Test</p>
        </div>
      </div>
    </>
  );
}

export default MainPage;
