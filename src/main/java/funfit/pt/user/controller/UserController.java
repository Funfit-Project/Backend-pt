package funfit.pt.user.controller;

import funfit.pt.responseDto.SuccessResponse;
import funfit.pt.user.dto.JoinRequest;
import funfit.pt.user.dto.JoinResponse;
import funfit.pt.user.dto.JwtDto;
import funfit.pt.user.dto.LoginRequest;
import funfit.pt.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/join")
    public ResponseEntity join(@RequestBody JoinRequest joinRequest) {
        JoinResponse joinResponse = userService.join(joinRequest);
        return ResponseEntity.status(HttpStatus.OK)
                        .body(new SuccessResponse("사용자 회원가입 성공", joinResponse));
    }

    @PostMapping("/api/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        JwtDto jwtDto = userService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse("사용자 로그인 성공", jwtDto));
    }
}
