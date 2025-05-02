"use client";

import type React from "react";
import { useState, useEffect, useRef } from "react";
import { Client, type IMessage } from "@stomp/stompjs";
import SockJS from "sockjs-client";

import { MessageCircle, Send, AlertCircle } from "lucide-react";
import type { UUID } from "crypto";

interface MessageOutput {
  id?: UUID;
  content: string;
  sent_at: string;
  username: string;
}

export default function LiveChat() {
  const [messages, setMessages] = useState<MessageOutput[]>([]);
  const [newMessage, setNewMessage] = useState("");
  const [connectionError, setConnectionError] = useState(false);
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const stompClientRef = useRef<Client | null>(null);
  const username = getSubFromToken();

  useEffect(() => {
    const socket = new SockJS("http://localhost:8080/furia-livechat-websocket");

    const token = localStorage.getItem("token");

    const stompClient = new Client({
      webSocketFactory: () => socket,
      connectHeaders: {
        Authorization: `${token}`,
      },
      reconnectDelay: 5000,
      onConnect: () => {
        console.log("🟢 Conectado ao WebSocket");
        setConnectionError(false);

        getMessages();

        stompClient.subscribe("/topics/furia/livechat", (message: IMessage) => {
          const body: MessageOutput = JSON.parse(message.body);
          console.log("📨 Nova mensagem recebida:", body);
          setMessages((prev) => [...prev, body]);
        });
      },
      onStompError: (frame) => {
        console.error("🔴 Erro no STOMP:", frame);
        setConnectionError(true);
      },
    });

    stompClient.activate();
    stompClientRef.current = stompClient;

    return () => {
      stompClient.deactivate();
    };
  }, []);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const formatDate = (timestamp: string) => {
    const date = new Date(timestamp);
    return new Intl.DateTimeFormat("en-US", {
      hour: "numeric",
      minute: "numeric",
      hour12: true,
      month: "short",
      day: "numeric",
    }).format(date);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (newMessage.trim() === "") return;

    const messageToSend = {
      username: username,
      content: newMessage,
      sent_at: new Date().toISOString(),
    };

    stompClientRef.current?.publish({
      destination: "/furia/livechat/message",
      body: JSON.stringify(messageToSend),
    });

    setNewMessage("");
  };

  async function getMessages() {
    const token = localStorage.getItem("token");

    const options = {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: token ?? "",
      },
    };

    try {
      const response = await fetch("http://localhost:8080/messages", options);

      if (response.ok) {
        const listMessage: MessageOutput[] = await response.json();
        setMessages((prev) => [...prev, ...listMessage]);
      }
    } catch (error) {
      console.log(error);
    }
  }

  function getSubFromToken(): string | null {
    const token = localStorage.getItem("token");

    if (!token) return null;

    try {
      const payloadBase64 = token.split(".")[1]; // Pega o payload do JWT
      const payload = JSON.parse(atob(payloadBase64)); // Decodifica e converte em objeto
      return payload.sub || null;
    } catch (error) {
      console.error("Token inválido:", error);
      return null;
    }
  }

  return (
    <div className="flex flex-col items-center flex-grow justify-center w-full px-4">
      <div className="w-full max-w-5xl flex flex-col flex-grow bg-neutral-900/90 text-white rounded-xl overflow-hidden border border-neutral-800 shadow-lg backdrop-blur-sm">
        <div className="border-b border-neutral-800 py-4 px-6">
          <div className="text-xl font-bold flex gap-3 items-center">
            <MessageCircle
              size={22}
              className="text-white"
              strokeWidth={2.25}
            />
            <span>CS:GO</span>
            <span className="inline-flex ml-2 h-2 w-2 rounded-full bg-red-500"></span>
          </div>
        </div>

        <div className="flex-grow overflow-hidden scrollbar">
          <div className="h-[65vh] overflow-y-auto p-4 space-y-3">
            {connectionError && (
              <div className="bg-red-900/70 border border-red-700 text-white p-4 rounded-lg mb-4">
                <div className="flex items-center gap-2">
                  <AlertCircle className="h-4 w-4" />
                  <p className="font-semibold">Erro de Conexão</p>
                </div>
                <p className="mt-1 text-sm">
                  Falha ao conectar ao servidor de chat. Por favor, verifique
                  sua conexão e suas credencias e tente novamente.
                </p>
              </div>
            )}

            {messages.map((message) => (
              <div
                key={message.id}
                className={`flex items-start p-3 rounded-xl transition-all duration-200 ${
                  message.username === username
                    ? "bg-red-900/10 hover:bg-red-900/20 border border-red-900/20"
                    : "hover:bg-white/5 border border-neutral-800"
                }`}
              >
                <div className="flex flex-col w-full">
                  <div className="flex items-center space-x-3">
                    <span
                      className={`font-medium ${
                        message.username === "Você"
                          ? "text-red-500"
                          : "text-white"
                      }`}
                    >
                      {message.username}
                    </span>
                    <span className="text-xs text-neutral-500">
                      {formatDate(message.sent_at)}
                    </span>
                  </div>
                  <div className="mt-1 text-white">{message.content}</div>
                </div>
              </div>
            ))}
            <div ref={messagesEndRef} />
          </div>
        </div>

        <div className="px-4 py-4 border-t border-neutral-800 bg-black">
          <form onSubmit={handleSubmit} className="flex w-full space-x-2">
            <input
              value={newMessage}
              onChange={(e) => setNewMessage(e.target.value)}
              placeholder="Digite sua mensagem..."
              className="flex-grow bg-neutral-900 border border-neutral-800 rounded-md px-3 py-2 text-white placeholder:text-neutral-500 focus:outline-none focus:ring-1 focus:ring-red-500/50 focus:border-red-500/50"
            />
            <button
              className="bg-red-900 hover:bg-red-800 text-white px-4 py-2 rounded-md flex items-center gap-2 transition-colors"
              type="submit"
            >
              <Send className="h-4 w-4" />
              <span>Enviar</span>
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
