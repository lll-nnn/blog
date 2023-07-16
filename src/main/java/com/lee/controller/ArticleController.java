package com.lee.controller;

import com.github.pagehelper.PageInfo;
import com.lee.pojo.*;
import com.lee.service.*;
import com.lee.utils.SensitiveFilter;
import com.lee.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ArticleController {

    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private TagArticleMappingService tagArticleMappingService;
    @Autowired
    private UserService userService;
    @Autowired
    private HasApprovalService hasApprovalService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserFollowService userFollowService;
    @Autowired
    private ArticleTypeService articleTypeService;
    @Autowired
    private MessageService messageService;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @RequestMapping(value = "/article/editor", method = RequestMethod.GET)
    public String toEditor(Model model){
//        List<String> types = tagService.getAllTagGroup();
        List<String> types = articleTypeService.getAllTypes();
        List<Tag> allTag = tagService.getAllTag();
        model.addAttribute("types", types);
        model.addAttribute("tags", allTag);
        return "editor";
    }

    @RequestMapping(value = "/article/editor/{articleId}", method = RequestMethod.GET)
    public String editArticle(@PathVariable("articleId") Long articleId, Model model){
        ArticleWithBLOBs article = articleService.getArticleInfo(articleId);
        List<String> types = articleTypeService.getAllTypes();
        List<Tag> allTag = tagService.getAllTag();
        String articleType = articleTypeService.getTypeName(article.getTypeId());
        List<Tag> articleTags = tagArticleMappingService.getTagsByPostId(articleId);
        model.addAttribute("types", types);
        model.addAttribute("tags", allTag);
        model.addAttribute("article", article);
        model.addAttribute("articleType", articleType);
        if(articleTags.size() == 3){
            model.addAttribute("tag1", articleTags.get(0).getName());
            model.addAttribute("tag2", articleTags.get(1).getName());
            model.addAttribute("tag3", articleTags.get(2).getName());
        }
        if (articleTags.size() == 2){
            model.addAttribute("tag1", articleTags.get(0).getName());
            model.addAttribute("tag2", articleTags.get(1).getName());
            model.addAttribute("tag3", articleTags.get(1).getName());
        }
        if (articleTags.size() == 1){
            model.addAttribute("tag1", articleTags.get(0).getName());
            model.addAttribute("tag2", articleTags.get(0).getName());
            model.addAttribute("tag3", articleTags.get(0).getName());
        }

        return "edit_article";
    }

    @ResponseBody
    @RequestMapping(value = "/article/save", method = RequestMethod.POST)
    public Result saveArticle(HttpSession session, @RequestBody ArticleEdit articleEdit){
//        System.out.println(title+"123123" + markdown + "\n-----\n" + html + "\n--------\n" + tagGroup);
//        System.out.println(tag1 + tag2 + tag3);
        Result result = new Result();
//        if ( session.getAttribute("loginUser") == null){
//            result.setSuccess(false);
//            result.setMessage("未登录");
//            return result;
//        }
        User user = UserHolder.getUser();
        if(user.getRole().equals("USER") && articleEdit.getTagGroup().equals("公告")){
            result.setSuccess(false);
            result.setMessage("管理员才可以发布公告");
            return result;
        }
        ArticleWithBLOBs articleWithBLOBs = new ArticleWithBLOBs();
        articleWithBLOBs.setHtmlContent(articleEdit.getHtmlContent());
        articleWithBLOBs.setMarkdownContent(articleEdit.getMarkdownContent());
        articleWithBLOBs.setAuditState("PASS");
        articleWithBLOBs.setCategory("ARTICLE");
        articleWithBLOBs.setAuthorId(user.getId());
        articleWithBLOBs.setTitle(articleEdit.getTitle());
        articleWithBLOBs.setContentType("MARKDOWN");
        articleWithBLOBs.setViews(0L);
        articleWithBLOBs.setApprovals(0L);
        articleWithBLOBs.setComments(0L);
        Long typeId = articleTypeService.getIdByName(articleEdit.getTagGroup());
        articleWithBLOBs.setTypeId(typeId);
        articleWithBLOBs.setHeadImg("");
        articleWithBLOBs.setOfficial((byte)0);
        articleWithBLOBs.setTop((byte)0);
        articleWithBLOBs.setSort(1000);
        articleWithBLOBs.setMarrow((byte)0);
        articleWithBLOBs.setCommentId(0L);
        articleWithBLOBs.setIsDelete((byte)0);
        articleWithBLOBs.setCreateAt(new Date());
        articleWithBLOBs.setUpdateAt(new Date());

        List<String> tags = new ArrayList<>();
        tags.add(articleEdit.getTag1());
        tags.add(articleEdit.getTag2());
        tags.add(articleEdit.getTag3());
        tags = tags.stream().distinct().collect(Collectors.toList());
        List<TagArticleMapping> tams = new ArrayList<>();
        for (String t :
                tags) {
            TagArticleMapping mapping = new TagArticleMapping();
            List<Tag> tagList = tagService.getTagByName(t);
            mapping.setTagId(tagList.get(0).getId());
            mapping.setIsDelete((byte)0);
            mapping.setCreateAt(new Date());
            mapping.setUpdateAt(new Date());
            tams.add(mapping);
        }
        int res = articleService.insertOne(articleWithBLOBs, tams);
        if(res == 0){
            result.setSuccess(false);
            result.setMessage("添加失败");
            return result;
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/article/update", method = RequestMethod.POST)
    public Result articleSave(@RequestBody ArticleEdit article, HttpSession session){
        Result result = new Result();
//        if ( session.getAttribute("loginUser") == null){
//            result.setSuccess(false);
//            result.setMessage("未登录");
//            return result;
//        }
        User loginUser = UserHolder.getUser();
        if(loginUser.getRole().equals("USER") && article.getTagGroup().equals("公告")){
            result.setSuccess(false);
            result.setMessage("管理员才可以发布公告");
            return result;
        }
        ArticleWithBLOBs articleInfo = articleService.getArticleInfo(article.getArticleId());
        if (!articleInfo.getAuthorId().equals(loginUser.getId())){
            result.setSuccess(false);
            result.setMessage("用户文章不存在");
            return result;
        }
        articleInfo.setTitle(article.getTitle());
        articleInfo.setMarkdownContent(article.getMarkdownContent());
        articleInfo.setHtmlContent(article.getHtmlContent());
        Long typeId = articleTypeService.getIdByName(article.getTagGroup());
        articleInfo.setTypeId(typeId);
        int r2 =  articleService.updateArticle(articleInfo);
        if (r2 == 0){
            result.setSuccess(false);
            result.setMessage("更新失败");
            return result;
        }
        List<String> tags = new ArrayList<>();
        tags.add(article.getTag1());
        tags.add(article.getTag2());
        tags.add(article.getTag3());
        tags = tags.stream().distinct().collect(Collectors.toList());
        int r = tagArticleMappingService.updateArticleTags(tags, article.getArticleId());
        if (r == 0){
            result.setSuccess(false);
            result.setMessage("更新失败");
            return result;
        }

        return result;
    }

    @RequestMapping(value = "/article/{id}", method = RequestMethod.GET)
    public String articleInfo(@PathVariable("id") Long id, Model model, HttpSession session){
        boolean isLogin = false, hasApproval = false, hasFollow = false;
//        if(session.getAttribute("isLogin") != null){
//            isLogin = (boolean)session.getAttribute("isLogin");
//        }

        ArticleWithBLOBs article = articleService.getArticleInfo(id);
        Long articleId = article.getId();
        List<Tag> tagList = tagArticleMappingService.getTagsByPostId(articleId);
        Long authorId = article.getAuthorId();
        User user = userService.getUserById(authorId);
        Map<String, Object> posts = new HashMap<>();
        posts.put("id", article.getId());
        posts.put("title", article.getTitle());
        posts.put("authorAvatar", user.getAvatar());
        posts.put("authorId", user.getId());
        posts.put("authorName", user.getNickname());
        posts.put("createAt", article.getCreateAt());
        posts.put("views", article.getViews());
        posts.put("comments", article.getComments());
        posts.put("tags", tagList);
        posts.put("content", sensitiveFilter.filter(article.getHtmlContent()));
        posts.put("updateAt", article.getUpdateAt());
        posts.put("approvals", article.getApprovals());

        User loginUser = UserHolder.getUser();
        if(loginUser != null){
            hasApproval = hasApprovalService.getIsApproval(loginUser.getId(), articleId);
            hasFollow = userFollowService.getIsFollow(loginUser.getId(), authorId);
        }
        model.addAttribute("hasFollow", hasFollow);
        model.addAttribute("hasApproval", hasApproval);
        model.addAttribute("posts", posts);
//        model.addAttribute("isLogin", isLogin);
        model.addAttribute("postsType", "/article");

        //comment
        List<Map<String, Object>> commentList = new ArrayList<>();
        List<Comment> comments = commentService.getByArticleId(articleId);
        for (Comment c :
                comments) {
            Map<String, Object> comment = new HashMap<>();
            User user1 = userService.getUserById(c.getUserId());
            comment.put("commentator", user1);
            comment.put("content", sensitiveFilter.filter(c.getContent()));
            comment.put("createAt", c.getCreateAt());
            comment.put("id", c.getId());
            List<Map<String, Object>> replies = new ArrayList<>();
            List<Comment> replyComments = commentService.getByReplyId(Math.toIntExact(c.getId()));
            for (Comment com :
                    replyComments) {
                Map<String, Object> reply = new HashMap<>();
                reply.put("commentator", userService.getUserById(com.getUserId()));
                if(com.getReplyReplyId() != null){
                    Long replyReplyId = com.getReplyReplyId();
                    Comment replyReply = commentService.getByKey(replyReplyId);
                    reply.put("respondent", userService.getUserById(replyReply.getUserId()));
                }else{
                    Comment re = commentService.getByKey(com.getReplyId());
                    reply.put("respondent", userService.getUserById(re.getUserId()));
                }
                reply.put("content", sensitiveFilter.filter(com.getContent()));
                reply.put("createAt", com.getCreateAt());
                reply.put("id", com.getId());
                replies.add(reply);
            }
            comment.put("replies", replies);


            if(c.getReplyId()==null){
                commentList.add(comment);
            }


        }

        model.addAttribute("comments", commentList);

        //作者其他文章
        List<ArticleWithBLOBs> authArticleList = articleService.getAuthorArticle(authorId);
        model.addAttribute("authArticleList", authArticleList);

        //更新浏览量
        int res = articleService.updateViews(id);

        return "article";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String toSearch(String key){
        return "redirect:/search/" + key + "/p/1";
    }

    @RequestMapping(value = "/search/{key}/p/{pageNum}", method = RequestMethod.GET)
    public String searchArticle(@PathVariable("key")String key,@PathVariable("pageNum") Integer pageNum,
                                Model model, HttpSession session){
//        if (session.getAttribute("loginUser") != null){
//            User loginUser = (User)session.getAttribute("loginUser");
//            model.addAttribute("isLogin", true);
//            model.addAttribute("loginUser", loginUser);
//        }

        PageInfo<Map<String, Object>> pageInfo = articleService.getSearchArticle(pageNum, key);
        model.addAttribute("key", key);
        model.addAttribute("pageInfo", pageInfo);
        return "search";
    }

    @RequestMapping(value = "/tag/{tagName}/p/{pageNum}", method = RequestMethod.GET)
    public String tagArticle(@PathVariable("tagName") String tagName, HttpSession session,
                             @PathVariable("pageNum") Integer pageNum, Model model){
//        if (session.getAttribute("loginUser") != null){
//            User loginUser = (User)session.getAttribute("loginUser");
//            model.addAttribute("isLogin", true);
//            model.addAttribute("loginUser", loginUser);
//        }
        PageInfo<Map<String, Object>> pageInfo = articleService.getTagArticle(tagName, pageNum);

        model.addAttribute("tagName", tagName);
        model.addAttribute("tagDesc", "含有" + tagName + "标签的文章");
        model.addAttribute("pageInfo", pageInfo);
        return "tag-info";
    }

    @RequestMapping(value = "/type/{typeName}/p/{pageNum}", method = RequestMethod.GET)
    public String typeArticle(@PathVariable("typeName") String typeName, HttpSession session,
                              @PathVariable("pageNum") Integer pageNum, Model model){
//        boolean isLogin = false;
//        if (session.getAttribute("loginUser") != null){
//            isLogin =  (boolean)session.getAttribute("isLogin");
//            User loginUser = (User) session.getAttribute("loginUser");
//            model.addAttribute("loginUser", loginUser);
//        }

        List<ArticleType> typeList = articleTypeService.selectAllType();
        ArticleType at = new ArticleType();
        at.setName("全部文章");
        typeList.add(0, at);
        List<Tag> tagList = tagService.getAllTag();

        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>();
        if (typeName.equals("全部文章")){
            pageInfo = articleService.getArticlePage(pageNum);
        }else{
            pageInfo = articleService.getTypeArticle(pageNum, typeName);
        }

        model.addAttribute("typeSelected", typeName);
        model.addAttribute("typeList", typeList);
        model.addAttribute("usedTags", tagList);
        model.addAttribute("pageInfo", pageInfo);
//        model.addAttribute("isLogin", isLogin);

        return "index-type";
    }

    @ResponseBody
    @RequestMapping(value = "/comment-rest/create", method = RequestMethod.POST)
    public Result commentSave(@RequestBody Comment comment, HttpSession session){
        Result result = new Result();
        User loginUser = UserHolder.getUser();
        Long articleId = comment.getPostsId();
        comment.setUserId(loginUser.getId());
        comment.setIsDelete(false);
        comment.setCreateAt(new Date());
        comment.setUpdateAt(new Date());

        if(comment.getReplyId()!=null){
            Comment reply = commentService.getById(comment.getReplyId());
            if (reply == null){
                result.setSuccess(false);
                result.setMessage("error");
                return result;
            }
            if (!reply.getPostsId().equals(comment.getPostsId())){
                result.setSuccess(false);
                result.setMessage("文章不存在");
                return result;
            }
            if (reply.getReplyId() != null){
                comment.setReplyReplyId(Long.valueOf(reply.getId()));
                comment.setReplyId(reply.getReplyId());
            }
        }

        int res = commentService.insertOne(comment);
        if(res == 0){
            result.setSuccess(false);
            result.setMessage("评论失败");
            return result;
        }
        //更新评论数量
        int r = articleService.updateComments(articleId);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/posts-rest/delete/{id}", method = RequestMethod.POST)
    public Result articleDelete(@PathVariable("id")Long id){
        Result result = new Result();
        int r = articleService.deleteOne(id);
        if (r == 0){
            result.setSuccess(false);
            result.setMessage("删除失败");
            return result;
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/approval-rest/create/{id}", method = RequestMethod.POST)
    public Result<Long> approvalAdd(@PathVariable("id")Long id, HttpSession session){
        Result<Long> result = new Result<>();
        User loginUser = UserHolder.getUser();
        Long approvals = articleService.addApproval(id);
        result.setData(approvals);
        int r = hasApprovalService.insertOne(id, loginUser.getId());
        if (r == 0){
            result.setSuccess(false);
            result.setMessage("点赞失败");
            return result;
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/approval-rest/delete/{id}", method = RequestMethod.POST)
    public Result<Long> approvalCancel(@PathVariable("id")Long id, HttpSession session){
        Result<Long> result = new Result<>();
        User loginUser = UserHolder.getUser();
        Long approval = articleService.cancelApproval(id);
        result.setData(approval);
        int r = hasApprovalService.deleteOne(id, loginUser.getId());
        if (r == 0){
            result.setSuccess(false);
            result.setMessage("取消点赞失败");
            return result;
        }
        return result;
    }

}
