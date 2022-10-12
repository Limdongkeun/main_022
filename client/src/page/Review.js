import React from "react";
import { Link, useNavigate, useLocation } from 'react-router-dom';
import axios from "axios";
import styled from "styled-components";

export default function Review() {

    const navigate = useNavigate();

    const location = useLocation();

    const handleMoveFavoriteDetailPage = () => {
        navigate(-1)
    };

    // comment delete
    // delete error
    const handleDeleteReview = async () => {
        try {
            const response = await axios.delete(`ec2-3-38-101-126.ap-northeast-2.compute.amazonaws.com:8080/comments/${location.state.commentId}`)
            if (true) {
                console.log('후기 삭제')
                navigate(-1)
            }
        } catch (error) {
            console.log('error:', error)
        }
    };

    return (
        <ReviewContainer>
            <img
                src='https://raw.githubusercontent.com/eirikmadland/notion-icons/master/v5/icon4/ul-multiply.svg'
                alt='exitIcon'
                onClick={handleMoveFavoriteDetailPage}
                className='exitIcon'
            />
            <section className="review_id">
                <p>{location.state.memberId}</p>
            </section>
            <section className="review_section">
                <p>{location.state.comment}</p>
                <section className="button_section">
                    <button><Link to='/addReview'>후기 수정</Link></button>
                    <button onClick={handleDeleteReview}>후기 삭제</button>
                </section>
            </section>
        </ReviewContainer>
    )
}

const ReviewContainer = styled.div`
padding: 3rem;
height: 100vh;
display: flex;
flex-direction: column;

.exitIcon{
    margin-left: auto;
    width: 3rem;
}

.exitIcon:hover{
    transform: scale(1.1);
    transition: .1s;
}

.review_id{
    display: flex;
    flex-direction: column;
    margin-bottom: 2rem;

    p{
        margin: 1rem;
        border: 1px solid black;
        width: 10rem;
        height: 10rem;
        border-radius: 50%;
        text-align: center;
        line-height: 10rem;
        font-size: 1.5vmax;
    }
}

.review_section{
    border: 1px solid black;
    padding: 3rem;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    height: 25rem;

    p{
        font-size: 1.5vmax;
        margin-bottom: 1rem;
    }
    
    .button_section{
        display: flex;
        justify-content: flex-end;

        button{
            margin-left: 1.5rem;
            border: none;
            background-color: white;
            font-size: 1.5vmax;
            cursor: pointer;

            a{
                text-decoration: none;
                color: black;
            }
        }

        button:hover{
            transform: scale(1.1);
            transition: .1s;
            color: #238f51;

            a{
                color: #238f51;
            }
        }
    }
}
`
