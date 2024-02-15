package org.zerock.b01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.BoardListReplyCountDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.upload.BoardListAllDTO;
import org.zerock.b01.service.BoardService;

import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    @Value("${org.zerock.upload.path}")//import시, springframework로
    private String uploadPath;

    private final BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
//        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);
    }

    //등록처리//화면에서 get방식으로 보고
    @PreAuthorize("hasRole('USER')")//USER만 등록 가능하도록 함.
    @GetMapping("/register")
    public void registerGet(){

    }
    //등록처리//post 방식으로 처리  //@Valid 검증에 실패하면 다시 앞의 화면으로 돌아가도록 함..
    @PostMapping("/register")//@Valid에서 문제 발생하면 모든 에러를 errors 라는 이름으로 RedirectAttributes에 추가해서 전송
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        log.info("board POST register......");

        if(bindingResult.hasErrors()){//에러 발생 시..? redirect 되는 거 같고...

            log.info("has errors......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/board/register";
        }

        log.info(boardDTO);

        Long bno = boardService.register(boardDTO);

        redirectAttributes.addFlashAttribute("result", bno);

        return "redirect:/board/list";
    }

    //조회 처리 & 화면 개발 // 수정&삭제 처리 화면으로 넘어가도록 코드 수정
//    @GetMapping("/read")
//    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model) {
//
//        BoardDTO boardDTO = boardService.readOne(bno);
//
//        log.info(boardDTO);
//
//        model.addAttribute("dto", boardDTO);
//    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model) {

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
    }

    @PreAuthorize("principal.username == #boardDTO.writer")
    @PostMapping("/modify")
    public String modify(PageRequestDTO pageRequestDTO,
                         @Valid BoardDTO boardDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        log.info("board modify post......" + boardDTO);

        if (bindingResult.hasErrors()){
            log.info("has errors......");

            String link = pageRequestDTO.getLink();

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            redirectAttributes.addAttribute("bno", boardDTO.getBno());

            return "redirect:/board/modify?" + link;
        }

        boardService.modify(boardDTO);

        redirectAttributes.addFlashAttribute("result", "modified");

        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        return "redirect:/board/read";
    }

    //기존 삭제 방법 => 게시물 번호만으로 삭제 처리
    @PreAuthorize("principal.username == #boardDTO.writer") //스프링 시큐리티 활용 => 작성자를 추가로 설정하여 삭제 처리
    @PostMapping("/remove")
    public String remove(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {

        Long bno = boardDTO.getBno();
        log.info("remove post.." + bno);

        boardService.remove(bno);

        //게시물이 데이터베이스상에서 삭제되었다면 첨부파일 삭제
        log.info(boardDTO.getFileNames());
        List<String> fileNames = boardDTO.getFileNames();
        if (fileNames != null && fileNames.size() > 0){
            removeFiles(fileNames);
        }

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/board/list";
    }

    public void removeFiles(List<String> files) {

        for (String fileName:files) {

            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

            String resourceName = resource.getFilename();

            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());

                resource.getFile().delete();

                //섬네일이 존재한다면
                if (contentType.startsWith("image")) {
                    File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);

                    thumbnailFile.delete();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
