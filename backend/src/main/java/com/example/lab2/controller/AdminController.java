package com.example.lab2.controller;


import com.example.lab2.dao.BookTypeRepository;
import com.example.lab2.dao.LibraryRepository;
import com.example.lab2.dto.bookcopy.ShowBookCopyDTO;
import com.example.lab2.dto.record.*;
import com.example.lab2.exception.UploadException;
import com.example.lab2.request.borrow.BorrowBookRequest;
import com.example.lab2.request.borrow.BorrowReservedBookRequest;
import com.example.lab2.request.borrow.ReturnBookRequest;
import com.example.lab2.request.comment.DeleteCommentRequest;
import com.example.lab2.request.comment.DeleteReplyRequest;
import com.example.lab2.request.sensitive.AddToSensitiveRequest;
import com.example.lab2.request.sensitive.RemoveFromSensitiveRequest;
import com.example.lab2.request.upload.AddBookCopyRequest;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.request.upload.UploadNewBookRequest;
import com.example.lab2.service.*;
import com.example.lab2.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource(name = "uploadService")
    UploadService uploadService;

    @Resource(name = "borrowService")
    BorrowService borrowService;

    @Resource(name = "searchService")
    SearchService searchService;

    @Resource(name = "normalUserService")
    NormalUserService normalUserService;

    @Resource(name = "commentService")
    CommentService commentService;

    @Autowired
    LibraryRepository libraryRepository;

    @Autowired
    BookTypeRepository bookTypeRepository;

    @Resource(name = "adminService")
    AdminService adminService;

    /**
     * ????????????????????????????????????isbn??????????????????
     *
     * @param uploadNewBookRequest
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/uploadNewBook")
    public ResponseEntity<GeneralResponse> handleUpload(
            @ModelAttribute @Valid UploadNewBookRequest uploadNewBookRequest, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            throw new UploadException(
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()
            );
        }

        GeneralResponse response = uploadService.handleUpload(uploadNewBookRequest);
        return ResponseEntity.ok(response);
    }


    /**
     * ??????????????????????????????????????????
     *
     * @param addBookCopyRequest
     * @param bindingResult
     * @return
     */
    @PostMapping("/addBookCopy")
    public ResponseEntity<GeneralResponse> addBookCopy(@Valid @RequestBody AddBookCopyRequest addBookCopyRequest,

                                                       BindingResult bindingResult, HttpServletRequest request) {
        //????????????????????????????????????
        if (bindingResult.hasFieldErrors()) {
            throw new UploadException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage())
                    ;
        }

        GeneralResponse generalResponse = uploadService.addBookCopy(addBookCopyRequest);
        return ResponseEntity.ok(generalResponse);
    }


    /**
     * ??????????????????????????????????????????????????????????????????
     *
     * @param isbn
     * @return
     */
    @GetMapping("/showBookToUser")
    public ResponseEntity<HashMap<String, Object>> showBookToUser(@RequestParam("isbn") String isbn) {
        HashMap<String, Object> result = new HashMap<>();
        ShowBookCopyDTO bookCopy = searchService.getBookCopyByIsbn(isbn);
        result.put("book", bookCopy);
        return ResponseEntity.ok(result);
    }

    /**
     * ????????????????????????????????????
     *
     * @param borrowBookRequest
     * @param bindingResult
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/lendBookToUser")
    public ResponseEntity<GeneralResponse> lendBookToUser(
            @Valid @RequestBody BorrowBookRequest borrowBookRequest,
            BindingResult bindingResult,
            HttpServletRequest httpServletRequest) {

        //???????????????????????????????????????????????????
        if (bindingResult.hasFieldErrors()) {
            throw new IllegalArgumentException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        String token = httpServletRequest.getHeader("token");

        //????????????????????????????????????????????????
        Long adminLibraryID = JwtUtils.getLibraryID(token);
        //??????admin?????????
        String admin = JwtUtils.getUserName(token);

        //???????????????
        GeneralResponse generalResponse = borrowService.lendBookToUser(
                borrowBookRequest.getUniqueBookMarkList(),
                borrowBookRequest.getUsername(),
                adminLibraryID,
                admin
        );

        //????????????????????????
        return ResponseEntity.ok(generalResponse);

    }

    /**
     * ????????????????????????????????????
     *
     * @param borrowReservedBookRequest
     * @param bindingResult
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/lendReservedBookToUser")
    public ResponseEntity<GeneralResponse> lendReservedBookToUser(@Valid @RequestBody BorrowReservedBookRequest borrowReservedBookRequest,
                                                                  BindingResult bindingResult,
                                                                  HttpServletRequest httpServletRequest) {

        if (bindingResult.hasFieldErrors()) {
            throw new IllegalArgumentException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        String token = httpServletRequest.getHeader("token");

        //?????????????????????????????????????????????
        Long adminLibraryID = JwtUtils.getLibraryID(token);
        //??????admin?????????
        String admin = JwtUtils.getUserName(token);

        GeneralResponse generalResponse = borrowService.lendReservedBookToUser(
                borrowReservedBookRequest.getUsername(),
                borrowReservedBookRequest.getUniqueBookMarkList(),
                adminLibraryID, admin
        );

        //????????????????????????
        return ResponseEntity.ok(generalResponse);

    }

    @PostMapping("/receiveBookFromUser")
    public ResponseEntity<List<String>> receiveBookFromUser(@Valid @RequestBody ReturnBookRequest returnBookRequest, BindingResult bindingResult,
                                                            HttpServletRequest httpServletRequest) {

        if (bindingResult.hasFieldErrors()) {
            throw new IllegalArgumentException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        String token = httpServletRequest.getHeader("token");

        //?????????????????????????????????????????????
        Long adminLibraryID = JwtUtils.getLibraryID(token);
        //??????admin?????????
        String admin = JwtUtils.getUserName(token);

        List<String> resList = normalUserService.returnBooks(returnBookRequest.getUniqueBookMarkList(), adminLibraryID, admin);

        //????????????????????????
        return ResponseEntity.ok(resList);
    }

    @GetMapping("/record")
    public ResponseEntity<HashMap<String, Object>> searchRecordByUsername(@RequestParam("username") String username) {
        List<ReserveRecordDTO> reserveRecordDTOS = normalUserService.getReserveRecord(username);
        List<BorrowRecordDTO> borrowRecordDTOS = normalUserService.getBorrowRecord(username);
        List<ReturnRecordDTO> returnRecordDTOS = normalUserService.getReturnRecord(username);
        List<FineRecordDTO> fineRecordDTOS = normalUserService.getFineRecord(username);

        //??????result
        HashMap<String, Object> result = new HashMap<>();
        result.put("reserveRecordList", reserveRecordDTOS);
        result.put("borrowRecordList", borrowRecordDTOS);
        result.put("returnRecordList", returnRecordDTOS);
        result.put("fineRecordList", fineRecordDTOS);
        return ResponseEntity.ok(result);

    }

    @GetMapping("/recordOfBook")
    public ResponseEntity<HashMap<String, Object>> getRecordByUniqueBookMark(@Param("isbn") String isbn) {

        List<RecordAboutBookCopyDTO> recordAboutBookCopyDTOS = normalUserService.getBookCopyRecord(isbn);
        //??????result
        HashMap<String, Object> result = new HashMap<>();
        result.put("recordList", recordAboutBookCopyDTOS);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/sensitiveWordList")
    public ResponseEntity<HashMap<String, Object>> getSensitiveWordList() {
        //?????????????????????????????????
        List<String> sensitiveWordList = adminService.findAllSensitiveWord();

        //??????????????????????????????
        HashMap<String, Object> res = new HashMap<>();
        res.put("sensitiveWordList", sensitiveWordList);

        //???????????????
        return ResponseEntity.ok(res);
    }


    /**
     * ??????????????????????????????????????????????????????
     *
     * @param addToSensitiveRequest
     * @return
     */
    @PostMapping("/addToSensitive")
    public ResponseEntity<GeneralResponse> addToSensitive(@RequestBody AddToSensitiveRequest addToSensitiveRequest) {

        List<String> addToSensitiveList = addToSensitiveRequest.getAddToSensitiveList();

        String msg = adminService.addToSensitive(addToSensitiveList);

        return ResponseEntity.ok(new GeneralResponse(msg));

    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param removeFromSensitiveRequest
     * @return
     */
    @PostMapping("/removeFromSensitive")
    public ResponseEntity<GeneralResponse> removeFromSensitive(
            @RequestBody RemoveFromSensitiveRequest removeFromSensitiveRequest) {

        List<String> removeFromSensitiveList = removeFromSensitiveRequest.getRemoveFromSensitiveList();

        String msg = adminService.removeFromSensitive(removeFromSensitiveList);

        return ResponseEntity.ok(new GeneralResponse(msg));
    }

    @PostMapping("/deleteComment")
    public ResponseEntity<?> deleteComment(@RequestBody DeleteCommentRequest deleteCommentRequest, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        String username = JwtUtils.getUserName(token);
        GeneralResponse generalResponse = commentService.deleteComment(deleteCommentRequest.getCommentID(), username);
        return ResponseEntity.ok(generalResponse);

    }

    @PostMapping("/deleteReply")
    public ResponseEntity<?> deleteReply(@RequestBody DeleteReplyRequest deleteReplyRequest, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        String username = JwtUtils.getUserName(token);
        GeneralResponse generalResponse = commentService.deleteReply(deleteReplyRequest.getCommentID(), username);
        return ResponseEntity.ok(generalResponse);

    }
}
