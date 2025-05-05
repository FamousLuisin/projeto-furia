"use client";

import type React from "react";

import { useState } from "react";
import { AlertCircle, Loader2, UserPlus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useNavigate } from "react-router-dom";
import Header from "./header";

export default function RegisterPage() {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [cpf, setCpf] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [username, setUsername] = useState("");
  const [birthDate, setBirthDate] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      setError("Passwords do not match");
      return;
    }

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
        first_name: firstName,
        last_name: lastName,
        cpf: cpf,
        birth_date: birthDate,
        username: username,
      }),
    };

    try {
      const API_URL = import.meta.env.VITE_API_URL;
      await fetch(`${API_URL}/register`, options);

      navigate("/login");
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
    } catch (err) {
      setError("Failed to register. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  const formatCPF = (value: string) => {
    const digits = value.replace(/\D/g, "");
    let formatted = digits;

    if (digits.length > 3) {
      formatted = `${digits.slice(0, 3)}.${digits.slice(3)}`;
    }
    if (digits.length > 6) {
      formatted = `${formatted.slice(0, 7)}.${formatted.slice(7)}`;
    }
    if (digits.length > 9) {
      formatted = `${formatted.slice(0, 11)}-${formatted.slice(11, 13)}`;
    }

    return formatted.slice(0, 14);
  };

  const handleCPFChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setCpf(formatCPF(e.target.value));
  };

  return (
    <>
      <Header />
      <div className="flex flex-col items-center flex-grow justify-center w-full px-4">
        <div className="w-full max-w-md flex flex-col bg-neutral-900/90 text-white rounded-xl overflow-hidden border border-neutral-800 shadow-lg backdrop-blur-sm">
          <div className="border-b border-neutral-800 py-4 px-6">
            <div className="text-xl font-bold flex gap-3 items-center">
              <UserPlus size={22} className="text-white" strokeWidth={2.25} />
              <span>Register</span>
            </div>
          </div>

          <form
            autoComplete="off"
            onSubmit={handleSubmit}
            className="flex flex-col p-6 space-y-5"
          >
            {error && (
              <div className="bg-red-900/70 border border-red-700 text-white p-4 rounded-lg">
                <div className="flex items-center gap-2">
                  <AlertCircle className="h-4 w-4" />
                  <p className="font-semibold">Registration Error</p>
                </div>
                <p className="mt-1 text-sm">{error}</p>
              </div>
            )}

            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="firstName" className="text-white">
                  First Name
                </Label>
                <Input
                  id="firstName"
                  type="text"
                  value={firstName}
                  onChange={(e) => setFirstName(e.target.value)}
                  required
                  className="flex-grow bg-neutral-900 border border-neutral-800 rounded-md px-3 py-2 text-white placeholder:text-neutral-500 focus:outline-none focus:ring-1 focus:ring-neutral-500 focus:border-neutral-500"
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="lastName" className="text-white">
                  Last Name
                </Label>
                <Input
                  id="lastName"
                  type="text"
                  value={lastName}
                  onChange={(e) => setLastName(e.target.value)}
                  required
                  className="flex-grow bg-neutral-900 border border-neutral-800 rounded-md px-3 py-2 text-white placeholder:text-neutral-500 focus:outline-none focus:ring-1 focus:ring-neutral-500 focus:border-neutral-500"
                />
              </div>
            </div>

            <div className="space-y-2">
              <Label htmlFor="username" className="text-white">
                Username
              </Label>
              <Input
                id="username"
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
                className="flex-grow bg-neutral-900 border border-neutral-800 rounded-md px-3 py-2 text-white placeholder:text-neutral-500 focus:outline-none focus:ring-1 focus:ring-neutral-500 focus:border-neutral-500"
              />
            </div>

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
              <Label htmlFor="cpf" className="text-white">
                CPF
              </Label>
              <Input
                id="cpf"
                type="text"
                placeholder="XXX.XXX.XXX-XX"
                value={cpf}
                onChange={handleCPFChange}
                maxLength={14}
                required
                className="flex-grow bg-neutral-900 border border-neutral-800 rounded-md px-3 py-2 text-white placeholder:text-neutral-500 focus:outline-none focus:ring-1 focus:ring-neutral-500 focus:border-neutral-500"
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="birthDate" className="text-white">
                Birth Date
              </Label>
              <Input
                id="birthDate"
                type="date"
                value={birthDate}
                onChange={(e) => setBirthDate(e.target.value)}
                required
                className="flex-grow bg-neutral-900 border border-neutral-800 rounded-md px-3 py-2 text-white placeholder:text-neutral-500 focus:outline-none focus:ring-1 focus:ring-neutral-500 focus:border-neutral-500"
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="password" className="text-white">
                Password
              </Label>
              <Input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                className="flex-grow bg-neutral-900 border border-neutral-800 rounded-md px-3 py-2 text-white placeholder:text-neutral-500 focus:outline-none focus:ring-1 focus:ring-neutral-500 focus:border-neutral-500"
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="confirmPassword" className="text-white">
                Confirm Password
              </Label>
              <Input
                id="confirmPassword"
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
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
                  <span>Creating account...</span>
                </div>
              ) : (
                "Create Account"
              )}
            </Button>
          </form>

          <div className="px-6 pb-6 text-center text-sm border-t border-neutral-800 pt-4">
            Already have an account?{" "}
            <a href="/login" className="text-neutral-400 hover:underline">
              Sign in
            </a>
          </div>
        </div>
      </div>
    </>
  );
}
