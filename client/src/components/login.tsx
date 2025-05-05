"use client";

import type React from "react";

import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { AlertCircle, Loader2, LogIn } from "lucide-react";
import Header from "./header";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setIsLoading(true);

    const options = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        email: email,
        password: password,
      }),
    };

    try {
      let api_url = import.meta.env.VITE_API_URL;

      if (api_url === undefined) {
        api_url = "http://localhost:8080";
      }

      const response = await fetch(`${api_url}/login`, options);

      if (response.ok) {
        const data = await response.json();
        console.log("Login bem-sucedido:", data);

        const token = response.headers.get("Authorization");

        if (token !== null) {
          localStorage.setItem("token", token);
          console.log(token);
        }

        navigate("/");
      } else {
        setError("Invalid email or password");
      }
    } catch (err) {
      setError("An error occurred during login");
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <Header />
      <div className="flex flex-col items-center flex-grow justify-center w-full px-4">
        <div className="w-full max-w-md flex flex-col bg-neutral-900/90 text-white rounded-xl overflow-hidden border border-neutral-800 shadow-lg backdrop-blur-sm">
          <div className="border-b border-neutral-800 py-4 px-6">
            <div className="text-xl font-bold flex gap-3 items-center">
              <LogIn size={22} className="text-white" strokeWidth={2.25} />
              <span>Login</span>
            </div>
          </div>

          <form
            autoComplete="off"
            onSubmit={handleSubmit}
            className="flex flex-col p-6 space-y-6"
          >
            {error && (
              <div className="bg-red-900/70 border border-red-700 text-white p-4 rounded-lg">
                <div className="flex items-center gap-2">
                  <AlertCircle className="h-4 w-4" />
                  <p className="font-semibold">Authentication Error</p>
                </div>
                <p className="mt-1 text-sm">{error}</p>
              </div>
            )}

            <div className="space-y-2">
              <Label htmlFor="email" className="text-white">
                Email
              </Label>
              <Input
                id="email"
                type="email"
                placeholder="name@example.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                className="flex-grow bg-neutral-900 border border-neutral-800 rounded-md px-3 py-2 text-white placeholder:text-neutral-500 focus:outline-none focus:ring-1 focus:ring-neutral-500 focus:border-neutral-500"
              />
            </div>

            <div className="space-y-2">
              <div className="flex items-center justify-between">
                <Label htmlFor="password" className="text-white">
                  Password
                </Label>
                <a
                  href="/forgot-password"
                  className="text-sm text-neutral-400 hover:text-white transition-colors"
                >
                  Forgot password?
                </a>
              </div>
              <Input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                className="flex-grow bg-neutral-900 border border-neutral-800 rounded-md px-3 py-2 text-white placeholder:text-neutral-500 focus:outline-none focus:ring-1 focus:ring-neutral-500 focus:border-neutral-500"
              />
            </div>

            <Button
              className="bg-neutral-800 hover:bg-neutral-700 text-white border border-neutral-700 px-4 py-2 rounded-md flex items-center gap-2 transition-colors"
              type="submit"
              disabled={isLoading}
            >
              {isLoading ? (
                <div className="flex items-center justify-center gap-2">
                  <Loader2 className="h-4 w-4 animate-spin" />
                  <span>Logging in...</span>
                </div>
              ) : (
                "Sign in"
              )}
            </Button>
          </form>

          <div className="px-6 pb-6 text-center text-sm border-t border-neutral-800 pt-4">
            Don&apos;t have an account?{" "}
            <a href="/register" className="text-neutral-400 hover:underline">
              Sign up
            </a>
          </div>
        </div>
      </div>
    </>
  );
}
