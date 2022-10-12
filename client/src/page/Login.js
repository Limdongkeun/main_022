import React, { useState } from "react";
import { Link, useNavigate } from 'react-router-dom';
import axios from "axios";
import styled from "styled-components";

export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const navigate = useNavigate();

    const URL = 'ec2-3-38-101-126.ap-northeast-2.compute.amazonaws.com:8080/members/login'

    const handleEmail = (event) => {
        setEmail(event.target.value);
    }

    const handlePassword = (event) => {
        setPassword(event.target.value);
    }

    const handleLogin = async () => {
        await axios.post(URL,
            ({
                email: email,
                password: password,
            }))
            .then(response => {
                localStorage.setItem("token", response.headers["access-token"]);
                console.log('로그인 성공');
                navigate('/main');
            })
            .catch(error =>
                console.log('error:', error)
            )
    }

    return (
        <LoginContainer>
            <Link to='/main' className="logo">구르미</Link>
            <form onSubmit={(event) => event.preventDefault()} className='login-form'>
                <label>Email</label>
                <input
                    type='email'
                    id='userEmail'
                    autoComplete='off'
                    required
                    value={email}
                    onChange={handleEmail}
                >
                </input>
                <label>password</label>
                <input
                    type='password'
                    id='userPassword'
                    autoComplete='off'
                    required
                    value={password}
                    onChange={handlePassword}
                >
                </input>
                <button type='submit' className="login_btn" onClick={handleLogin}>Log in</button>
            </form>
            <Link to='/signup'>회원가입하러 가기</Link>
        </LoginContainer>
    )
}

const LoginContainer = styled.div`
border: 1px solid black;
height: 100vh;
display: flex;
flex-direction: column;
justify-content: center;
align-items: center;

.logo{
    text-decoration: none;
    font-size: 2vmax;
    color: #238f51;
    padding: 1rem;
    margin-top: -10rem;
    margin-bottom: 3rem;
    }

.login-form{
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    width: 20rem;
    height: 13rem;

    
    input, .login_btn{
        height: 2rem;
        border-radius: .5rem;
        border: 1px solid black;
        outline-color: #238f51;
        padding: 0 .5rem;
    }

    .login_btn{
        height: 2.5rem;
        border: none;
        background-color: #238f51;
        color: white;
        font-size: 1vmax;
    }

    .login_btn:hover{
        font-size: 1.1vmax;
    }
}

a{
    margin: 1rem;
    text-decoration: none;
    color: gray;
}

a:hover{
    color: #238f51;
}
`
