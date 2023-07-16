package com.lee.controller;

import com.github.pagehelper.PageInfo;
import com.lee.pojo.Article;
import com.lee.pojo.ArticleType;
import com.lee.pojo.Tag;
import com.lee.pojo.User;
import com.lee.service.ArticleService;
import com.lee.service.ArticleTypeService;
import com.lee.service.TagService;
import com.lee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;
    @Autowired
    private ArticleTypeService articleTypeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String toIndex(){

        return "redirect:/1";
    }

    @RequestMapping(value = "/{pageNum1}", method = RequestMethod.GET)
    public String index(@PathVariable("pageNum1") Integer pageNum1, HttpSession session, Model model){
//        boolean isLogin = false;
//        if(session.getAttribute("isLogin") != null){
//            isLogin = (boolean)session.getAttribute("isLogin");
//        }
//        User user = (User) session.getAttribute("loginUser");

        List<ArticleType> typeList = articleTypeService.selectAllType();
        ArticleType at = new ArticleType();
        at.setName("全部文章");
        typeList.add(0, at);
        List<Tag> tagList = tagService.getAllTag();

        PageInfo<Map<String, Object>> pageInfo = articleService.getArticlePage(pageNum1);
        model.addAttribute("typeSelected", "全部文章");
//        model.addAttribute("isLogin", isLogin);
//        model.addAttribute("loginUser", user);
        model.addAttribute("typeList", typeList);
        model.addAttribute("usedTags", tagList);
        model.addAttribute("pageInfo", pageInfo);


        return "index";
    }
}
