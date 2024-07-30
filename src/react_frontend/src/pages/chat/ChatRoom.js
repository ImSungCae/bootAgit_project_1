import React, { useState, useEffect, useRef } from 'react';
import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { useParams, useNavigate } from 'react-router-dom';
import { useUser } from '../common/AuthContext';
import { Container, Row, Col, Form, Button, Card } from 'react-bootstrap';
import { GoArrowLeft } from "react-icons/go";
import './ChatRoom.css'; // CSS 파일을 import 합니다

const ChatRoom = () => {
    const { authState } = useUser();
    const { username } = authState;
    const { roomId } = useParams();
    const navigate = useNavigate();
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState('');
    const [stompClient, setStompClient] = useState(null);
    const messagesEndRef = useRef(null);

    useEffect(() => {
        const socket = new SockJS('http://localhost:8080/ws');
        const client = Stomp.over(socket);

        client.connect({}, () => {
            client.subscribe(`/topic/${roomId}`, (msg) => {
                const message = JSON.parse(msg.body);
                setMessages((prevMessages) => [...prevMessages, message]);
            });

            client.send(`/app/chat.addUser/${roomId}`, {}, JSON.stringify({
                sender: username,
                type: 'JOIN'
            }));
        });

        setStompClient(client);

        return () => {
            if (client) {
                client.disconnect();
            }
        };
    }, [roomId]);

    const sendMessage = () => {
        if (stompClient) {
            const chatMessage = {
                sender: username,
                content: message,
                type: 'CHAT'
            };
            stompClient.send(`/app/chat.sendMessage/${roomId}`, {}, JSON.stringify(chatMessage));
            setMessage('');
        }
    };

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    return (
        <Container fluid>
            <Row className="justify-content-md-center">
                <Col md={6}>
                    <Card className="mt-3">
                        <Card.Header>
                            <GoArrowLeft onClick={() => navigate('/chatr')} style={{ cursor: 'pointer' }} />
                             &nbsp; Chat Room: {roomId}
                        </Card.Header>
                        <Card.Body>
                            <div className="message-container">
                                {messages.map((msg, index) => (
                                    <div
                                        key={index}
                                        className={`message-bubble ${msg.sender === username ? 'message-bubble-right' : 'message-bubble-left'}`}
                                    >
                                        <strong>{msg.sender}</strong>: {msg.content}
                                    </div>
                                ))}
                                <div ref={messagesEndRef} />
                            </div>
                            <Form.Group className="mt-3">
                                <Form.Control
                                    type="text"
                                    placeholder="Type your message..."
                                    value={message}
                                    onChange={(e) => setMessage(e.target.value)}
                                    onKeyDown={(e) => e.key === 'Enter' && sendMessage()}
                                />
                            </Form.Group>
                            <Button variant="primary" onClick={sendMessage} className="mt-2">
                                Send
                            </Button>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default ChatRoom;