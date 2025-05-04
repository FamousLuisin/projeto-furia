import { useEffect, useState } from "react";

export default function Header() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem("token");
    setIsLoggedIn(!!token);
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    setIsLoggedIn(false);
  };
  return (
    <header className="w-11/12 max-w-5xl py-4 space-y-2">
      {/* Desktop Navigation */}
      <div className="items-center justify-between md:flex hidden">
        <div className="flex items-center gap-2">
          <a href="/" className="text-white font-bold text-3xl tracking-tight">
            Furia
          </a>
          <nav className="flex items-center ml-8 space-x-6">
            <a
              href="/"
              className="text-white/80 hover:text-white text-sm font-medium transition-colors"
            >
              Home
            </a>

            {!isLoggedIn ? (
              <>
                <a
                  href="/register"
                  className="text-white/80 hover:text-white text-sm font-medium transition-colors"
                >
                  Register
                </a>
                <a
                  href="/login"
                  className="text-white/80 hover:text-white text-sm font-medium transition-colors"
                >
                  Login
                </a>
              </>
            ) : (
              <a
                href="/"
                onClick={handleLogout}
                className="text-white/80 hover:text-white text-sm font-medium transition-colors"
              >
                Logout
              </a>
            )}
          </nav>
        </div>
      </div>

      {/* Mobile Navigation - Restructured with logo above links */}
      <div className="md:hidden flex flex-col items-center space-y-3">
        <a href="/" className="text-white font-bold text-3xl tracking-tight">
          Furia
        </a>
        <div className="flex items-center justify-center space-x-6">
          <a
            href="/"
            className="text-white/80 hover:text-white text-sm font-medium transition-colors"
          >
            Home
          </a>

          {!isLoggedIn ? (
            <>
              <a
                href="/register"
                className="text-white/80 hover:text-white text-sm font-medium transition-colors"
              >
                Register
              </a>
              <a
                href="/login"
                className="text-white/80 hover:text-white text-sm font-medium transition-colors"
              >
                Login
              </a>
            </>
          ) : (
            <a
              href="/"
              onClick={handleLogout}
              className="text-white/80 hover:text-white text-sm font-medium transition-colors"
            >
              Logout
            </a>
          )}
        </div>
      </div>

      <div className="h-px bg-gradient-to-r from-transparent via-white/20 to-transparent" />
    </header>
  );
}
