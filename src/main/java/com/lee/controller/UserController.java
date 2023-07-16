package com.lee.controller;


import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.lee.pojo.*;
import com.lee.service.ArticleService;
import com.lee.service.MessageService;
import com.lee.service.UserFollowService;
import com.lee.service.UserService;
import com.lee.utils.UserHolder;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.lee.utils.RedisConstants.LOGIN_TTL;
import static com.lee.utils.RedisConstants.USER_LOGIN_KEY;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserFollowService userFollowService;
    @Autowired
    private ArticleService articleService;

    @Autowired
    private MessageService messageService;

    @Resource
    private StringRedisTemplate redisTemplate;

    @ResponseBody
    @RequestMapping(value = "/user-rest/register", method = RequestMethod.POST)
    public Result userRegister(@RequestBody User user){
        Result result = new Result();
        String email = user.getEmail();
        boolean used = userService.getHasEmail(email);
        if (used){
            result.setMessage("邮箱已存在");
            result.setSuccess(false);
            return result;
        }
        user.setRole("USER");
        user.setState("ENABLE");
        user.setSex("UNKNOWN");
        user.setSource("REGISTER");
        user.setCreateAt(new Date());
        user.setLastLoginTime(new Date());
        user.setUpdateAt(new Date());
        user.setAvatar("https://raw.githubusercontent.com/lll-nnn/picture/master/picgo/202211202328823.png");
        user.setSignature("");
        user.setIsDelete((byte) 0);
        int r = userService.insertUser(user);
        if(r == 0){
            result.setSuccess(false);
            result.setMessage("注册失败");
            return result;
        }
        result.setSuccess(true);
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/user-rest/login", method = RequestMethod.POST)
    public Result userLogin(@RequestBody User user, HttpSession session, HttpServletResponse response){
        Result result = new Result();
        String email = user.getEmail(), password = user.getPassword();
        User userByEmail = userService.getUserByEmail(email);
        if(userByEmail == null){
            result.setMessage("用户不存在");
            result.setSuccess(false);
            return result;
        }
        if (userByEmail.getIsDelete() == 1){
            result.setSuccess(false);
            result.setMessage("用户已被禁用");
            return result;
        }
        if(userByEmail.getPassword().equals(password)){
//            session.setAttribute("loginUser", userByEmail);
//            session.setAttribute("isLogin", true);
            String uuid = cn.hutool.core.lang.UUID.randomUUID().toString(true);
            String redisKey = USER_LOGIN_KEY + uuid;
            redisTemplate.opsForValue().set(redisKey, JSONUtil.toJsonStr(userByEmail)
                    , LOGIN_TTL, TimeUnit.MINUTES);
            Cookie cookie = new Cookie("token", uuid);
            cookie.setMaxAge(30 * 60);
            cookie.setPath("/classwork");
            response.addCookie(cookie);
            userByEmail.setLastLoginTime(new Date());
            int res = userService.updateInfo(userByEmail);
            result.setSuccess(true);
        }else{
            result.setMessage("密码错误");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value = "/user/logout", method = RequestMethod.GET)
    public String userLogout(HttpServletRequest request, HttpServletResponse response){
//        session.removeAttribute("loginUser");
//        session.removeAttribute("isLogin");
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")){
                String token = cookie.getValue();
                redisTemplate.delete(USER_LOGIN_KEY + token);
                cookie.setValue(null);
                cookie.setMaxAge(0);
                cookie.setPath("/classwork");
                response.addCookie(cookie);
            }
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public String getUserById(@PathVariable("id") Long id, Model model, HttpSession session){
        User user = userService.getUserById(id);
        boolean hasFollow = false;
        User loginUser = UserHolder.getUser();
        if(loginUser != null){
            hasFollow = userFollowService.getIsFollow(loginUser.getId(), id);
//            model.addAttribute("loginUser", loginUser);
        }
        List<Map<String, Object>> articleList = articleService.getAllArticle(id);
        List<User> fansList;
        fansList = userService.getFans(id);
        List<User> followerList;
        followerList = userService.getFollowers(id);
        model.addAttribute("user", user);
//        model.addAttribute("isLogin", isLogin);
        model.addAttribute("hasFollow", hasFollow);
        model.addAttribute("articleList", articleList);
        model.addAttribute("fansList", fansList);
        model.addAttribute("followerList", followerList);

        return "user";
    }

    @ResponseBody
    @RequestMapping(value = "/user-rest/update-headimg", method = RequestMethod.POST)
    public Result uploadHeadImg(@RequestBody MultipartFile file, HttpSession session, HttpServletRequest request) throws IOException {
        Result result = new Result();
        String filename = file.getOriginalFilename();
        String ext = filename.substring(filename.lastIndexOf('.'));
        //uuid
        String uuid = UUID.randomUUID().toString();
        //保证上传的文件名字不会相同
        filename = uuid + ext;
        //System.out.println(filename);
        //获取servletContext对象
        ServletContext servletContext = session.getServletContext();
        String uploadPath = servletContext.getRealPath("upload");
        File fileP = new File(uploadPath);
        //若目录不存在则创建
        if(!fileP.exists()){
            fileP.mkdir();
        }
        String realPath = uploadPath + File.separator + filename;
        String urlPath = request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort() + servletContext.getContextPath() + File.separator + "upload" + File.separator + filename;
//        System.out.println(uploadPath + "123123" + filename);
        file.transferTo(new File(realPath));
        User user = UserHolder.getUser();
        user.setAvatar(urlPath);
        int r = userService.updateInfo(user);
        if (r == 0){
            result.setSuccess(false);
            result.setMessage("上传失败");
            return result;
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/user-rest/update-info", method = RequestMethod.POST)
    public Result updateInfo(@RequestBody User user, HttpSession session){
        Result result = new Result();
        User loginUser = UserHolder.getUser();
        loginUser.setUpdateAt(new Date());
        loginUser.setEmail(user.getEmail());
        loginUser.setSignature(user.getSignature());
        loginUser.setNickname(user.getNickname());
        int r = userService.updateInfo(loginUser);
        if (r == 0){
            result.setSuccess(false);
            result.setMessage("更新失败");
            return result;
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/user-rest/update-pwd", method = RequestMethod.POST)
    public Result updatePwd(@RequestBody Password password, HttpSession session){
        Result result = new Result();
        User loginUser = UserHolder.getUser();
        if (!password.getOldPassword().equals(loginUser.getPassword())){
            result.setSuccess(false);
            result.setMessage("旧密码错误");
            return result;
        }
        loginUser.setPassword(password.getNewPassword());
        int r = userService.updateInfo(loginUser);
        if (r == 0){
            result.setSuccess(false);
            result.setMessage("更新失败");
            return result;
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/user-rest/follow/{followedId}", method = RequestMethod.POST)
    public Result userFollow(@PathVariable("followedId") Long followedId, HttpSession session){
        Result result = new Result();
        User loginUser = UserHolder.getUser();
        UserFollow userFollow = new UserFollow();
        userFollow.setFollowed(followedId);
        userFollow.setFollower(loginUser.getId());
        userFollow.setFollowedType("USER");
        userFollow.setIsDelete(false);
        userFollow.setCreateAt(new Date());
        userFollow.setUpdateAt(new Date());
        int r = userFollowService.followOne(userFollow);
        if (r == 0){
            result.setSuccess(false);
            result.setMessage("关注失败");
            return result;
        }
        long sender = loginUser.getId();
        long receiver = followedId;
        messageService.insertFollow(sender, receiver);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/user-rest/cancel-follow/{followedId}", method = RequestMethod.POST)
    public Result userFollowCancel(HttpSession session, @PathVariable("followedId") Long followedId){
        Result result = new Result();
        User loginUser = UserHolder.getUser();
        int r = userFollowService.followCancel(loginUser.getId(), followedId);
        if (r == 0){
            result.setSuccess(false);
            result.setMessage("取消关注失败");
            return result;
        }
        return result;
    }










//    @RequestMapping(value = "/register", method = RequestMethod.GET)
//    public String toRegister(){
//        return "register";
//    }
//
//    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
//    public String userRegister(User user, HttpServletRequest request){
//        user.setRole("User");
//        user.setState("Enable");
//        user.setSex("UNKNOWN");
//        user.setSource("REGISTER");
//        user.setCreateAt(new Date());
//        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
//        user.setLastLoginTime(new Date());
//        user.setAvatar("");
//        user.setSignature("");
//        user.setIsDelete((byte) 0);
//
//        int res = userService.insertUser(user);
//        if(res == 0){
//            request.setAttribute("msg", "注册失败");
//            return "redirect:/register";
//        }else{
//            request.setAttribute("msg", "注册成功");
//            return "redirect:/login";
//        }
//
//    }

//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public String toLogin(){
//        return "login";
//    }

//    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
//    public String userLogin(String email, String password, HttpSession session){
//        User user = userService.getUserByEmail(email);
//        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
//        if(user.getPassword().equals(password)){
//            session.setAttribute("isLogin", true);
//            session.setAttribute("loginUser", user);
//            //session.setAttribute("isAdmin", user.getRole());
//            return "redirect:/";
//        }else{
//            return "redirect:/login";
//        }
//    }

    @GetMapping("/message")
    public String message(HttpSession session, Model model){
        User user = (User)session.getAttribute("loginUser");
        List<String> typeList = new ArrayList<>();
        typeList.add("关注");
        typeList.add("评论");
        typeList.add("点赞");
        List<Map<String, Object>> msgList = new ArrayList<>();
        List<Message> messages = messageService.getByReceiver(user.getId());
        for (Message message : messages){
            Map<String, Object> map = new HashMap<>();
            map.put("read", message.getRead());
            User sender = userService.getUserById(Long.parseLong(message.getSender()));
            map.put("senderAvatar", sender.getAvatar());
            map.put("senderName", sender.getNickname());

        }

        model.addAttribute("typeList", typeList);
        model.addAttribute("loginUser", user);
        model.addAttribute("isLogin", true);
        return "message";
    }



}
