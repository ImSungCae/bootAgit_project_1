import React, { useState, useEffect } from 'react';
import { useUser } from '../common/AuthContext';
import { fetchTasks, createTask, updateTask, deleteTask } from '../api/TaskAPI';
import { Button, Form, Modal, Table, Container, InputGroup, FormControl, Dropdown, Pagination } from 'react-bootstrap';

const TaskPage = () => {
    const { authState } = useUser();
    const { username } = authState;
    const [tasks, setTasks] = useState([]);
    const [newTask, setNewTask] = useState({ title: '', description: '' });
    const [show, setShow] = useState(false);
    const [editTask, setEditTask] = useState(null);
    const [filter, setFilter] = useState('all');
    const [search, setSearch] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const [tasksPerPage] = useState(5);

    useEffect(() => {
        if (username) {
            fetchTasks(username)
                .then(setTasks)
                .catch((error) => console.log("할 일을 불러오는 데 실패했습니다.", error));
        }
    }, [username]);

    const handleAddTask = async () => {
        try {
            const task = await createTask(newTask, username);
            setTasks([...tasks, task]);
            setNewTask({ title: '', description: '' });
            setShow(false);
        } catch (error) {
            console.log("할 일을 생성하는 데 실패했습니다.", error.response ? error.response.data : error.message);
        }
    };

    const handleUpdateTask = async () => {
        try {
            const updatedTask = await updateTask(editTask.id, editTask, username);
            setTasks(tasks.map((task) => (task.id === editTask.id ? updatedTask : task)));
            setEditTask(null);
            setShow(false);
        } catch (error) {
            console.log("할 일을 수정하는 데 실패했습니다.", error.response ? error.response.data : error.message);
        }
    };

    const handleDeleteTask = async (taskId) => {
        try {
            await deleteTask(taskId, username);
            setTasks(tasks.filter((task) => task.id !== taskId));
        } catch (error) {
            console.log("할 일을 삭제하는 데 실패했습니다.", error.response ? error.response.data : error.message);
        }
    };

    const filteredTasks = tasks
        .filter((task) => {
            const matchesSearch = task.title.toLowerCase().includes(search.toLowerCase()) ||
                task.description.toLowerCase().includes(search.toLowerCase());
            const matchesFilter = filter === 'all' ||
                (filter === 'completed' && task.completed) ||
                (filter === 'incomplete' && !task.completed);
            return matchesSearch && matchesFilter;
        })
        .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt)); // 생성 날짜가 최신인 것부터 상단에 표시

    const indexOfLastTask = currentPage * tasksPerPage;
    const indexOfFirstTask = indexOfLastTask - tasksPerPage;
    const currentTasks = filteredTasks.slice(indexOfFirstTask, indexOfLastTask);

    const paginate = (pageNumber) => setCurrentPage(pageNumber);

    const formatDate = (dateString) => {
        const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' };
        return new Date(dateString).toLocaleString(undefined, options);
    };

    return (
        <Container>
            <h1 className="my-4">할 일 목록</h1>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <InputGroup className="w-50">
                    <FormControl
                        placeholder="검색어 입력"
                        aria-label="검색어 입력"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />
                    <Dropdown>
                        <Dropdown.Toggle variant="secondary" id="dropdown-basic">
                            {filter === 'all' ? '전체' : filter === 'completed' ? '완료' : '미완료'}
                        </Dropdown.Toggle>

                        <Dropdown.Menu>
                            <Dropdown.Item onClick={() => setFilter('all')}>전체</Dropdown.Item>
                            <Dropdown.Item onClick={() => setFilter('completed')}>완료</Dropdown.Item>
                            <Dropdown.Item onClick={() => setFilter('incomplete')}>미완료</Dropdown.Item>
                        </Dropdown.Menu>
                    </Dropdown>
                </InputGroup>
                <Button variant="primary" onClick={() => {
                    setShow(true);
                    setEditTask(null);  // 새로운 할 일 추가 시, editTask 초기화
                }}>
                    할 일 추가
                </Button>
            </div>
            <Table bordered hover className="mt-4">
                <thead className="table-dark">
                <tr>
                    <th>#</th>
                    <th>제목</th>
                    <th style={{ width: '30%' }}>할 일</th>
                    <th>생성 날짜</th>
                    <th>수정 날짜</th>
                    <th>상태</th>
                    <th>액션</th>
                </tr>
                </thead>
                <tbody>
                {currentTasks.length > 0 ? (
                    currentTasks.map((task, index) => (
                        <tr key={task.id} className={task.completed ? 'table-success' : ''}>
                            <td>{filteredTasks.length - (indexOfFirstTask + index)}</td> {/* 역순 번호 매기기 */}
                            <td>{task.title}</td>
                            <td>{task.description}</td>
                            <td>{task.createdAt ? formatDate(task.createdAt) : '없음'}</td>
                            <td>{task.updatedAt ? formatDate(task.updatedAt) : '없음'}</td>
                            <td>{task.completed ? '완료' : '미완료'}</td>
                            <td>
                                <Button variant="warning" onClick={() => {
                                    setEditTask(task);
                                    setShow(true);
                                }}>수정</Button>{' '}
                                <Button variant="danger" onClick={() => handleDeleteTask(task.id)}>삭제</Button>
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan="7">할 일이 없습니다.</td>
                    </tr>
                )}
                </tbody>
            </Table>
            <Pagination>
                {Array.from({ length: Math.ceil(filteredTasks.length / tasksPerPage) }, (_, index) => (
                    <Pagination.Item
                        key={index + 1}
                        active={index + 1 === currentPage}
                        onClick={() => paginate(index + 1)}
                    >
                        {index + 1}
                    </Pagination.Item>
                ))}
            </Pagination>

            <Modal show={show} onHide={() => setShow(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>{editTask ? '할 일 수정' : '할 일 추가'}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group>
                            <Form.Label>제목</Form.Label>
                            <Form.Control
                                type="text"
                                value={editTask ? editTask.title : newTask.title}
                                onChange={(e) => editTask ?
                                    setEditTask({ ...editTask, title: e.target.value }) :
                                    setNewTask({ ...newTask, title: e.target.value })
                                }
                            />
                        </Form.Group>
                        <Form.Group className="mt-3">
                            <Form.Label>할 일</Form.Label>
                            <Form.Control
                                type="text"
                                value={editTask ? editTask.description : newTask.description}
                                onChange={(e) => editTask ?
                                    setEditTask({ ...editTask, description: e.target.value }) :
                                    setNewTask({ ...newTask, description: e.target.value })
                                }
                            />
                        </Form.Group>
                        <Form.Group className="mt-3">
                            <Form.Check
                                type="checkbox"
                                label="완료"
                                checked={editTask ? editTask.completed : newTask.completed}
                                onChange={(e) => editTask ?
                                    setEditTask({ ...editTask, completed: e.target.checked }) :
                                    setNewTask({ ...newTask, completed: e.target.checked })
                                }
                            />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => {
                        setShow(false);
                        setEditTask(null);  // 모달 닫을 때 editTask 초기화
                    }}>
                        닫기
                    </Button>
                    <Button variant="primary" onClick={editTask ? handleUpdateTask : handleAddTask}>
                        {editTask ? '수정' : '추가'}
                    </Button>
                </Modal.Footer>
            </Modal>
        </Container>
    );
};

export default TaskPage;
