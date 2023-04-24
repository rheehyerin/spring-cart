package cart.controller;

import cart.domain.Member;
import cart.exception.AuthorizationException;
import cart.infrastructure.AuthorizationExtractor;
import cart.infrastructure.BasicAuthorizationExtractor;
import cart.service.AuthService;
import cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CartController {
    private AuthorizationExtractor<Member> basicAuthorizationExtractor = new BasicAuthorizationExtractor();
    private final AuthService authService;
    private final CartService cartService;
    @PostMapping("/cart/{productId}")
    public void addCart(HttpServletRequest request, @PathVariable String productId) {
        Member member = basicAuthorizationExtractor.extract(request);
        String email = member.getEmail();
        String password = member.getPassword();

        if (!authService.checkInvalidLogin(email, password)) {
            throw new AuthorizationException();
        }

        cartService.addCart(authService.findMember(email).getId(), Long.parseLong(productId));
    }
}
