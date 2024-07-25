import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { fetchTasks } from "../api/TaskAPI";
import { Button, ListGroup } from "react-bootstrap";

const TaskPage = () => {
    const { username } = useParams(); // URL에서 username을 가져옵니다.
    const [tasks, setTasks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const getTasks = async () => {
            try {
                console.log(username);
                const tasksData = await fetchTasks(username);
                setTasks(tasksData);
            } catch (error) {
                setError("할 일을 불러오는 데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };

        getTasks();
    }, [username]);

    if (loading) return <p>로딩 중...</p>;
    if (error) return <p>{error}</p>;

    return (
        <div>
            <h1>{username}의 할 일</h1>
            {tasks.length === 0 ? (
                <p>할 일이 없습니다.</p>
            ) : (
                <ListGroup>
                    {tasks.map(task => (
                        <ListGroup.Item key={task.id}>
                            <h5>{task.title}</h5>
                            <p>{task.description}</p>
                            <Button variant="primary">수정</Button>
                            <Button variant="danger">삭제</Button>
                        </ListGroup.Item>
                    ))}
                </ListGroup>
            )}
        </div>
    );
};

export default TaskPage;
