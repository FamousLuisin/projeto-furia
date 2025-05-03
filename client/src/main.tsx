import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.tsx";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Header from "./components/header.tsx";
import LoginPage from "./components/login.tsx";
import Footer from "./components/footer.tsx";
import RegisterPage from "./components/register.tsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
  },
  {
    path: "/login",
    element: <LoginPage />,
  },
  {
    path: "/register",
    element: <RegisterPage />,
  },
]);

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <div className="w-full min-h-screen h-full bg-neutral-950 flex flex-col items-center">
      <Header />
      <RouterProvider router={router} />
      <Footer />
    </div>
  </StrictMode>
);
