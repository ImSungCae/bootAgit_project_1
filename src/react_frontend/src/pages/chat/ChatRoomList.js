import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Button, ListGroup, Container, Modal } from 'react-bootstrap';
import { getChatRooms, createChatRoom } from '../api/ChatAPI';

const ChatRoomList = () => {
    const [chatRooms, setChatRooms] = useState([]);
    const [roomName, setRoomName] = useState('');
    const [maxUsers, setMaxUsers] = useState(10);
    const [isPublic, setIsPublic] = useState(true);
    const [password, setPassword] = useState('');
    const [showModal, setShowModal] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        getChatRooms().then((response) => {
            setChatRooms(response.data);
        }).catch(error => {
            console.error("Error fetching chat rooms:", error);
        });
    }, []);

    const handleCreateRoom = () => {
        createChatRoom(roomName, maxUsers, isPublic, password).then((response) => {
            setChatRooms([...chatRooms, response.data]);
            setShowModal(false);
            setRoomName('');
            setMaxUsers(10);
            setIsPublic(true);
            setPassword('');
        }).catch(error => {
            console.error("Error creating chat room:", error);
        });
    };

    const enterRoom = (roomId) => {
        navigate(`/chat/${roomId}`);
    };

    return (
        <Container>
            <h1 className="my-4">채팅방</h1>
            <Button variant="primary" onClick={() => setShowModal(true)}>
                채팅방 생성
            </Button>

            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>채팅방 생성</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group controlId="formRoomName" className="mb-3">
                            <Form.Label>Room Name</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Enter room name"
                                value={roomName}
                                onChange={(e) => setRoomName(e.target.value)}
                            />
                        </Form.Group>
                        <Form.Group controlId="formMaxUsers" className="mb-3">
                            <Form.Label>Max Users</Form.Label>
                            <Form.Control
                                type="number"
                                placeholder="Enter max users"
                                value={maxUsers}
                                onChange={(e) => setMaxUsers(parseInt(e.target.value))}
                            />
                        </Form.Group>
                        <Form.Group controlId="formIsPublic" className="mb-3">
                            <Form.Label>Room Type</Form.Label>
                            <Form.Control
                                as="select"
                                value={isPublic}
                                onChange={(e) => setIsPublic(e.target.value === 'true')}
                            >
                                <option value="true">Public</option>
                                <option value="false">Private</option>
                            </Form.Control>
                        </Form.Group>
                        {!isPublic && (
                            <Form.Group controlId="formPassword" className="mb-3">
                                <Form.Label>Password</Form.Label>
                                <Form.Control
                                    type="password"
                                    placeholder="Enter password"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                />
                            </Form.Group>
                        )}
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowModal(false)}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={handleCreateRoom}>
                        Create Room
                    </Button>
                </Modal.Footer>
            </Modal>

            <h2 className="my-4">채팅방 리스트</h2>
            <ListGroup>
                {chatRooms.map((room) => (
                    <ListGroup.Item key={room.id} action onClick={() => enterRoom(room.id)}>
                        {room.name} ({room.maxUsers})
                    </ListGroup.Item>
                ))}
            </ListGroup>
        </Container>
    );
};

export default ChatRoomList;
