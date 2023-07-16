package com.lee.controller;

import com.github.pagehelper.PageInfo;
import com.lee.pojo.User;
import com.lee.service.ArticleService;
import com.lee.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminIndex(HttpSession session, Model model){
//        if (!isAdmin(session)){
//            return "redirect:/";
//        }
//        model.addAttribute("isLogin", session.getAttribute("isLogin"));
//        model.addAttribute("loginUser", session.getAttribute("loginUser"));
        return "admin";
    }

    @RequestMapping(value = "/admin/user/{pageNum}", method = RequestMethod.GET)
    public String userManage(@PathVariable("pageNum")Integer pageNum, HttpSession session, Model model){
//        if (!isAdmin(session)){
//            return "redirect:/";
//        }
        PageInfo<User> userPage = userService.getUserPage(pageNum);

        model.addAttribute("page", userPage);
//        model.addAttribute("isLogin", session.getAttribute("isLogin"));
//        model.addAttribute("loginUser", session.getAttribute("loginUser"));
        return "admin-user";
    }

    @RequestMapping(value = "/admin/userDelete/{userId}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable("userId") Long userId, HttpSession session){
//        if (!isAdmin(session)){
//            return "redirect:/";
//        }
        int r = userService.deleteUser(userId);//set isDelete=1

        return "redirect:/admin/user/1";
    }

    @RequestMapping(value = "/admin/userUnDelete/{userId}", method = RequestMethod.DELETE)
    public String unDeleteUser(@PathVariable("userId")Long userId, HttpSession session){
//        if (!isAdmin(session)){
//            return "redirect:/";
//        }
        User userById = userService.getUserById(userId);
        userById.setIsDelete((byte)0);
        int r = userService.updateInfo(userById);

        return "redirect:/admin/user/1";
    }

    @RequestMapping(value = "/admin/user-admin/{userId}", method = RequestMethod.GET)
    public String setUserToAdmin(@PathVariable("userId")Long userId, HttpSession session){
//        if (!isAdmin(session)){
//            return "redirect:/";
//        }
        User user = userService.getUserById(userId);
        user.setRole("ADMIN");
        int r = userService.updateInfo(user);

        return "redirect:/admin/user/1";
    }

    @RequestMapping(value = "/admin/admin-user/{userId}", method = RequestMethod.GET)
    public String setAdminToUser(@PathVariable("userId")Long userId,HttpSession session){
//        if (!isAdmin(session)){
//            return "redirect:/";
//        }
        User user = userService.getUserById(userId);
        user.setRole("USER");
        userService.updateInfo(user);
        return "redirect:/admin/user/1";
    }


    //文章管理
    @GetMapping("/admin/article/{pageNum}")
    public String adminArticles(@PathVariable("pageNum")Integer pageNum, HttpSession session, Model model){
//        if (!isAdmin(session)){
//            return "redirect:/";
//        }
        PageInfo<Map<String, Object>> articlePage = articleService.getArticlePage(pageNum);

//        model.addAttribute("isLogin", session.getAttribute("isLogin"));
//        model.addAttribute("loginUser", session.getAttribute("loginUser"));
        model.addAttribute("page", articlePage);
        return "admin-article";
    }

    @RequestMapping(value = "/admin/articleDelete/{articleId}", method = RequestMethod.DELETE)
    public String deleteArticle(@PathVariable("articleId")Long articleId, HttpSession session){
//        if (!isAdmin(session)){
//            return "redirect:/";
//        }
        int r = articleService.deleteOne(articleId);
        return "redirect:/admin/article/1";
    }





    public boolean isAdmin(HttpSession session){
        boolean isLogin = false;

        if (session.getAttribute("loginUser") == null || session.getAttribute("isLogin") == null){
            return false;
        }
        User user = (User)session.getAttribute("loginUser");
        if (user.getRole().equals("USER")){
            return false;
        }
        return true;
    }
}
