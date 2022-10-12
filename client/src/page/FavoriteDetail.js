import React, { useState, useEffect } from "react";
import { Link, useNavigate, useLocation } from 'react-router-dom';
import axios from "axios";
import styled from "styled-components";

import LikeDislike from "../component/LikeDislike";

export default function FavoriteDetail() {
    const [modal, setModal] = useState({
        text: '',
        isFindOpen: false,
        isUrlOpen: false,
        isFavoriteOpen: false,
        isFormOpen: false,
    });

    const [commentList, setCommentList] = useState([]);

    const navigate = useNavigate();

    const location = useLocation();

    // isLike = false >>> like 값이 안 들어왔을 때도 흰색으로 표시
    const likeImage = (isLike = false) => isLike ? 'https://raw.githubusercontent.com/eirikmadland/notion-icons/master/v5/icon4/mt-thumb_up.svg' : 'https://raw.githubusercontent.com/eirikmadland/notion-icons/master/v5/icon4/mi-thumbs-up.svg';

    const hateImage = (isUnLike = false) => isUnLike ? 'https://raw.githubusercontent.com/eirikmadland/notion-icons/master/v5/icon4/mt-thumb_down.svg' : 'https://raw.githubusercontent.com/eirikmadland/notion-icons/master/v5/icon4/mi-thumbs-down.svg';

    const copyUrl = `http://localhost:3000${location.pathname}`;

    const { writeText } = window.navigator.clipboard;

    const handleMoveFavoritePage = () => {
        navigate(`/favorite`);
    };

    const handleFindRoot = () => {
        setModal(() => ({
            text: '서비스 준비중입니다',
            isFindOpen: true
        }))
    };

    const handleCopyModal = () => {
        setModal(() => ({
            text: 'url이 복사되었습니다',
            isUrlOpen: true
        }))
    };

    const handleCopyUrl = () => {
        setModal(false)
        writeText(copyUrl).then(() => {
            console.log('복사 완료')
        })
    };

    const handleOpenChangeInfoForm = () => {
        setModal(() => ({
            isFormOpen: true
        }))
    };

    const handleSubmitInfo = () => {
        alert('정보수정이 요청되었습니다 👍')
        setModal(false)
    }

    // 같은 동작을 해도 각각 해당되는 동작을 넣어주기 객체안에 데이터를 넣어주는 !
    // 요소마다 like 이벤트를 개별로
    // setLike Boolean 변경 >>> 새로운 배열 생성
    // data 속성을 이용해서 img id 값을 받아왔다
    // 실무에서 다른 점 >>> 서버로 날아간다

    // 개별 like 반영
    const handleLikeImage = (event) => {
        // tag data-## >>> dataset 객체에 data 저장 / data-id >>> id 라는 객체에 저장
        const { id } = event.target.dataset

        // 몇번째 요소 like 인지 확인
        const targetIndex = commentList.findIndex(comment => comment.commentId === id)

        // targetIndex 해당 되는 요소만 true 로 변경
        const updatedComment = {
            ...commentList[targetIndex],
            isLike: true,
            isHate: true
        }

        // 얕은 복사로 새로운 배열 생성 targetIndex 번째를 제외하고 만든다 0부터 그 전까지
        // ...을 붙인 이유 >>> 타겟 되는 것을 제외하고 그대로 false 상태이다 / 타겟만 true
        const newCommentList = [...commentList.slice(0, targetIndex), updatedComment, ...commentList.slice(targetIndex + 1)]

        // 실제로 서버에 먼저
        setCommentList(newCommentList)
    };

    // get
    useEffect(() => {
        const getReviewList = async () => {
            const response = await axios.get('http://localhost:4000/comments');
            // server get
            // const response = await axios.get('http://ec2-54-180-29-60.ap-northeast-2.compute.amazonaws.com:8080/comments');

            const newCommentList = response.data.map(commentInfo => {
                return {
                    ...commentInfo,
                    isLike: false
                }
            })

            setCommentList(newCommentList)
        }
        getReviewList();
    }, []);

    // post
    // 임시로 시설명만 저장한다 >>> 어떤 데이터 저장할지 의논 /// 내 생각엔 위도, 경도 필요하다
    const handleAddFavorite = () => {
        setModal(() => ({
            text: '즐겨찾기에 저장되었습니다',
            isFavoriteOpen: true
        }))

        const addFavoriteList = async () => {
            try {
                const response = await axios.post('ec2-3-38-101-126.ap-northeast-2.compute.amazonaws.com:8080/favoriteList', {
                    시설명: location.state.place.시설명
                });
                if (response) {
                    console.log('즐겨찾기 저장 성공')
                }
            } catch (err) {
                console.log('error:', err)
            }
        }

        addFavoriteList();
    }

    return (
        <DetailContainer>
            <img
                src='https://raw.githubusercontent.com/eirikmadland/notion-icons/master/v5/icon4/ul-multiply.svg'
                alt='exitIcon'
                onClick={handleMoveFavoritePage}
                className='exit_icon'
            />
            <section className="place_section">
                <p>운영중</p>
                <img
                    src='https://raw.githubusercontent.com/eirikmadland/notion-icons/master/v5/icon4/mi-plug.svg'
                    alt='placeIcon'
                />
            </section>
            <section className="button_section">
                <button onClick={handleFindRoot}>길찾기</button>
                {modal.isFindOpen && (<div className='modal_container' onClick={() => setModal(false)}>
                    <img
                        src='https://raw.githubusercontent.com/eirikmadland/notion-icons/master/v5/icon1/ul-multiply.svg'
                        alt='exit_icon'
                        className="exit_icon"
                    />
                    <p>{modal.text}</p>
                </div>)}
                <button onClick={handleCopyModal}>공유하기</button>
                {modal.isUrlOpen && (<div className='modal_container' onClick={handleCopyUrl}>
                    <img
                        src='https://raw.githubusercontent.com/eirikmadland/notion-icons/master/v5/icon1/ul-multiply.svg'
                        alt='exit_icon'
                        className="exit_icon"
                    />
                    <p>{modal.text}</p>
                </div>)}
                <button onClick={handleAddFavorite}>즐겨찾기</button>
                {modal.isFavoriteOpen && (<div className='modal_container' onClick={() => setModal(false)}>
                    <img
                        src='https://raw.githubusercontent.com/eirikmadland/notion-icons/master/v5/icon1/ul-multiply.svg'
                        alt='exit_icon'
                        className="exit_icon_white"
                    />
                    <p>{modal.text}</p>
                </div>)}
                <button><Link to='/addReview'>후기작성</Link></button>
            </section>
            <section className='place_detail'>
                <button onClick={handleOpenChangeInfoForm}>정보수정요청</button>
                <p>{location.state.place.시설명}</p>
                <p>{location.state.place.소재지지번주소}</p>
                <p>운영시간<br />{location.state.place.평일운영시작시각}~{location.state.place.평일운영종료시각}</p>
                <p>대표번호<br />{location.state.place.관리기관전화번호}</p>
            </section>
            {
                modal.isFormOpen &&
                <section className="change_form">
                    <form>
                        <img
                            src='https://raw.githubusercontent.com/eirikmadland/notion-icons/master/v5/icon4/ul-multiply.svg'
                            alt='exitIcon'
                            onClick={() => setModal(false)}
                            className='exit_icon'
                        />
                        <p className="changeInfo">정보수정</p>
                        <label>장소명</label>
                        <input
                            type='text'
                            required
                        />
                        <label>위치</label>
                        <input
                            type='text'
                            required
                        />
                        <label>운영시간</label>
                        <input
                            type='text'
                            required
                        />
                        <label>전화번호</label>
                        <input
                            type='text'
                            required
                        />
                        <label>기타 수정사항</label>
                        <input
                            type='text'
                            required
                        />
                        <input
                            type='submit'
                            value='수정사항 요청하기'
                            onClick={handleSubmitInfo}
                            className='submit_input'
                        />
                    </form>
                </section>
            }
            {modal.isFormSubmitOpen && (<div className='modal' onClick={() => setModal(false)}>{modal.text}</div>)}
            <section className="comment_section">
                <p className="comment_count">전체 {commentList.length}</p>
                <ul>
                    {commentList.map(({ commentId, memberId, comment, isLike }) => {
                        return (
                            <div key={commentId}>
                                < li className="commentList" >
                                    <img
                                        src='https://raw.githubusercontent.com/eirikmadland/notion-icons/master/v5/icon4/mt-face.svg'
                                        alt='user_icon'
                                        className="user_icon"
                                    />
                                    <section className="comment_detail_section">
                                        <button value={commentId}>
                                            <Link
                                                to={`/review/${commentId}`}
                                                state={{ commentId, memberId, comment }}
                                            >
                                                {memberId}
                                            </Link>
                                        </button>
                                        <p>
                                            <Link
                                                to={`/review/${commentId}`}
                                                state={{ commentId, memberId, comment }}
                                            >
                                                {comment}
                                            </Link>
                                        </p>

                                        {/* <section className="recommend_setcion">
                                            <img
                                                src={likeImage(isLike)}
                                                alt='likeImage'
                                                onClick={handleLikeImage}
                                                className="likeImage"
                                                data-id={commentId}
                                            />
                                            <span>추천</span>
                                        </section> */}
                                        <LikeDislike />
                                    </section>
                                </li>
                                <hr></hr>
                            </div>
                        )
                    })}
                </ul>
            </section >
        </DetailContainer >
    )
}

const DetailContainer = styled.div`
display: flex;
flex-direction: column;
justify-content: center;
align-items: center;
padding: 3rem;

.exit_icon, .exit_icon_white{
    width: 3rem;
    margin-left:auto;
}

.exit_icon:hover, .exit_icon_white:hover{
    transform: scale(1.1);
    transition: .1s;
}

.likeImage{
    width: 2rem;
    margin: 0;
}

.place_section{
    width: 10rem;
    height: 10rem;
    margin: 3rem;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    p{
        font-size: 1.5vmax;
    }

    img{
        width: 100%;
        background-color: #fec126;
        padding: 1rem;
        margin: 1rem;
        border-radius: 1rem;
    }
}

.button_section{
    width: 35rem;
    margin: 0 auto;
    display: flex;
    justify-content: space-around;

    button{
        border: none;
        background-color: white;
        font-size: 2vmax;
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

.modal_container{
    border-radius: 1rem;
    background-color: #238f51;
    color: white;
    font-size: 2vmax;
    width: 45rem;
    height: 20rem;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: center;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    position: fixed;

    img{
        margin: 0 auto;
        margin-top: -5rem;
    }

    img:hover{
        cursor: pointer;
    }

    p{
        margin-top: 4rem;
    }
}

.place_detail{
    border-radius: 1rem;
    background-color: whitesmoke;
    display: flex;
    flex-direction: column;
    width: 40rem;
    padding: 3rem;
    margin:2rem;

    button{
        margin-left: auto;
        border: none;
        background-color: whitesmoke;
        font-size: 1.5vmax;
        cursor: pointer;
    }

    button:hover{
        transform: scale(1.1);
        transition: .1s;
        color: #f05d4d;
    }

    p{
        margin: .5rem 0;
        font-size: 1.5vmax;
    }
}

.comment_section{
    width: 40rem;
    
    .comment_count{
        font-size: 1.5vmax;
    }

    ul{
        .commentList{
            width: 100%;
            display: flex;
            padding: 1rem;

            .user_icon{
                width: 7rem;
                margin: 0;
            }

            .comment_detail_section{
                width: 100%;
                padding: 1rem;

                button{
                    border: none;
                    background-color: white;
                    font-size: 1.5vmax;
                    margin: 1rem 0;
                }

                button:hover{
                    transform: scale(1.1);
                    transition: .1s;
                    
                    a{
                        color: #238f51;
                    }
                }

                p:hover{
                    a{
                        color: #238f51;
                        transition: .1s;
                    }
                }

                a{
                text-decoration: none;
                color: black;
                margin: .5rem 0;
                }
                

                .recommend_setcion{
                    display: flex;
                    align-items: center;
                    margin: .5rem 0;

                    .likeImage:hover{
                        cursor: pointer;
                    }
                }
            }
        }
    }
}

.change_form{
    position: absolute;
    top: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(102, 100, 100, 0.3);
    display: flex;
    justify-content: center;
    align-items: center;

    form{
        border-radius: 1rem;
        width: 20rem;
        height: 30rem;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        padding: 1rem;
        background-color: white;

        img{
            width: 2rem;
        }

        img:hover{
            cursor: pointer;
        }

        p{
            font-size: 1.3vmax;
            margin-top: -1rem;
        }

        label{
            font-size: 1vmax;
            margin-bottom: -.5rem;
        }
        input{
            height: 2rem;
            padding: 1rem .5rem;
            border-radius: .5rem;
            border: 1px solid black;
            outline-color: #f05d4d;
        }

        .submit_input{
            padding: 0;
            border: none;
            background-color: #f05d4d;
            color: white;
        }

        .submit_input:hover{
            transform: scale(1.05);
        }
    }
}



.changeInfo{
    text-align: center;
}

.commentList{
    /* border: 1px solid black; */
    list-style: none;
    width: 25rem;
    padding: 2rem;
}
`
