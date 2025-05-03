import { MessageSquare } from "lucide-react";
import { Button } from "./components/ui/button";
import LiveChatModal from "./components/chat";
import { useState } from "react";

function App() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  return (
    <>
      <div className="flex flex-col items-center justify-center flex-grow w-full px-4">
        <div className="text-center space-y-6">
          <h1 className="text-4xl font-bold text-white">Welcome to Furia</h1>
          <p className="text-white/70 max-w-lg">
            Connect with our CS:GO community and get real-time updates and chat
            support.
          </p>
          <Button
            onClick={() => setIsModalOpen(true)}
            className="bg-neutral-800 hover:bg-neutral-700 text-white border border-neutral-700"
          >
            <MessageSquare className="mr-2 h-4 w-4" />
            Open Chat
          </Button>
        </div>
      </div>

      <LiveChatModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
      />
    </>
  );
}

export default App;
