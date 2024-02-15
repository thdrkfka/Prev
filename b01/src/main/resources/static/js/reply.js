async function get1(bno) {//Axios를 이용해서 Ajax 통신하는 부분

    const result = await axios.get(`/replies/list/${bno}`)

    // console.log(result) //console에 출력해서 axios가 호출되는지 확인함.

    // return result.data

    return result;

}

async function getList({bno, page, size, goLast}) {

    const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}})

    if (goLast) {
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total/size))
        return getList({bno:bno, page:lastPage, size:size})
    }

    return result.data

}

//새로운 댓글 등록하는 기능
async function addReply(replyObj) {//파라미터를 JS의 객체로 받아 axios.post를 이용해 전달
    const response = await axios.post(`/replies/`,replyObj)
    return response.data
}

//댓글 조회(GET 방식) & 수정(PUT 방식)
//댓글 조회
async function getReply(rno) {
    const response = await axios.get(`/replies/${rno}`)
    return response.data
}
//댓글 수정
async function modifyReply(replyObj){
    const response = await axios.put(`/replies/${replyObj.rno}`, replyObj)
    return response.data
}

//댓글 삭제
async function removeReply(rno){
    const response = await axios.delete(`/replies/${rno}`)
    return response.data
}