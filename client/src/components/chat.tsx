"use client";

import type React from "react";
import { useState, useEffect, useRef } from "react";
import { Client, type IMessage } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { MessageCircle, Send, AlertCircle, X } from "lucide-react";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";
import type { UUID } from "crypto";

interface MessageOutput {
  id?: UUID;
  content: string;
  sent_at: string;
  username: string;
  is_bot?: boolean;
}

interface LiveChatModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export default function LiveChatModal({ isOpen, onClose }: LiveChatModalProps) {
  const [messages, setMessages] = useState<MessageOutput[]>([]);
  const [newMessage, setNewMessage] = useState("");
  const [connectionError, setConnectionError] = useState(false);
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const stompClientRef = useRef<Client | null>(null);
  const username = getSubFromToken();

  useEffect(() => {
    if (!isOpen) return;

    const API_URL = process.env.REACT_APP_API_URL;
    const socket = new SockJS(`${API_URL}/furia-livechat-websocket`);
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
  }, [isOpen]);

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

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (newMessage.trim() === "") return;

    const messageToSend = {
      username: username,
      content: newMessage,
      sent_at: new Date().toISOString(),
    };

    if (newMessage.startsWith("@bot ")) {
      const chatbotMessage = {
        username: username,
        content: newMessage,
        sent_at: new Date().toISOString(),
      };

      try {
        const token = localStorage.getItem("token");
        setNewMessage("");

        const API_URL = process.env.REACT_APP_API_URL;
        const response = await fetch(`${API_URL}/chatbot`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `${token}`,
          },
          body: JSON.stringify(chatbotMessage),
        });

        if (response.ok) {
          const botResponse: MessageOutput[] = await response.json();
          console.log("Resposta do chatbot:", botResponse);
          setMessages((prev) => [...prev, ...botResponse]);
        } else {
          console.error("Erro ao enviar mensagem para o chatbot.");
          setNewMessage("");
        }
      } catch (error) {
        console.error("Erro ao enviar mensagem para o chatbot:", error);
        setNewMessage("");
      }
    } else {
      stompClientRef.current?.publish({
        destination: "/furia/livechat/message",
        body: JSON.stringify(messageToSend),
      });
      setNewMessage("");
    }
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

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black/70 flex items-center justify-center z-50 p-4">
      <div className="w-full max-w-2xl flex flex-col bg-neutral-900 text-white rounded-xl overflow-hidden border border-neutral-800 shadow-lg animate-in fade-in duration-300">
        <div className="border-b border-neutral-800 py-3 px-4 flex justify-between items-center">
          <div className="text-lg font-bold flex gap-3 items-center">
            <MessageCircle
              size={20}
              className="text-white"
              strokeWidth={2.25}
            />
            <span>CS:GO</span>
            <span className="inline-flex ml-2 h-2 w-2 rounded-full bg-green-500"></span>
          </div>
          <Button
            variant="ghost"
            size="icon"
            onClick={onClose}
            className="text-neutral-400 hover:text-white hover:bg-neutral-800"
          >
            <X size={20} />
          </Button>
        </div>

        <div className="flex-grow overflow-hidden scrollbar">
          <div className="h-[50vh] overflow-y-auto p-4 space-y-3">
            {connectionError && (
              <div className="bg-neutral-800 border border-neutral-700 text-white p-4 rounded-lg mb-4">
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
                className={cn(
                  "flex items-start p-3 rounded-xl transition-all duration-200",
                  message.username === username
                    ? "bg-neutral-800 border border-neutral-700 ml-8"
                    : "bg-neutral-900 border border-neutral-700 mr-8",
                  message.is_bot && "bg-blue-400 border-blue-300 mr-8"
                )}
              >
                <div className="flex flex-col w-full">
                  <div className="flex items-center space-x-3">
                    <span
                      className={cn(
                        "font-medium",
                        message.username === username
                          ? "text-white"
                          : "text-neutral-300",
                        message.is_bot && "text-white"
                      )}
                    >
                      {message.username}
                    </span>
                    <span
                      className={cn(
                        "text-xs text-neutral-500",
                        message.is_bot && "text-neutral-50"
                      )}
                    >
                      {formatDate(message.sent_at)}
                    </span>
                  </div>
                  <div className="mt-1 text-white whitespace-pre-line">
                    {message.content}
                  </div>
                </div>
              </div>
            ))}
            <div ref={messagesEndRef} />
          </div>
        </div>

        <div className="px-4 py-4 border-t border-neutral-800">
          <form
            onSubmit={handleSubmit}
            className="flex items-center w-full space-x-2"
          >
            <input
              value={newMessage}
              onChange={(e) => setNewMessage(e.target.value)}
              placeholder="Digite sua mensagem..."
              className="flex-grow bg-neutral-900 border border-neutral-800 rounded-md px-3 py-2 text-white placeholder:text-neutral-500 focus:outline-none focus:ring-1 focus:ring-neutral-500 focus:border-neutral-500"
            />
            <Button
              className="bg-neutral-800 hover:bg-neutral-700 text-white border border-neutral-700 px-4 py-2 rounded-md flex items-center gap-2 transition-colors"
              type="submit"
            >
              <Send className="h-4 w-4" />
              <span>Enviar</span>
            </Button>
          </form>
        </div>
      </div>
    </div>
  );
}
