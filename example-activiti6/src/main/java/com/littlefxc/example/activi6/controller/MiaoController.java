package com.littlefxc.example.activi6.controller;

import com.littlefxc.example.activi6.entity.User;
import com.littlefxc.example.activi6.entity.VacationForm;
import com.littlefxc.example.activi6.service.MiaoService;
import com.littlefxc.example.activi6.util.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author fengxuechao
 */
@Controller
public class MiaoController {

    @Autowired
    private MiaoService miaoService;

    @RequestMapping("/")
    public String login() {
        return "login";
    }

    //申请者的首页
    @RequestMapping("/home")
    public String index(ModelMap model, HttpServletRequest request) {
        List<VacationForm> forms = miaoService.formList();
        Cookie[] cookies = request.getCookies();
        String user = "";
        //从cookie中获取当前用户
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userInfo".equals(cookie.getName())) {
                    user = cookie.getValue();
                    break;
                }
            }
        }
        List<VacationForm> formsMap = new ArrayList<VacationForm>();
        for (VacationForm form : forms) {
            //申请者只能看到自己申请的请假单信息
            if (user.equals(form.getApplicant())) {
                formsMap.add(form);
            }
        }
        //将forms参数返回
        model.addAttribute("forms", formsMap);
        return "index";
    }

    //审核者的首页
    @RequestMapping("/homeApprover")
    public String indexApprover(ModelMap model) {
        List<VacationForm> forms = miaoService.formList();
        List<VacationForm> formsMap = new ArrayList<VacationForm>();
        for (VacationForm form : forms) {
            //审核者只能看到待审核状态的请假单
            if ("领导审核".equals(form.getState())) {
                formsMap.add(form);
            }
        }
        model.addAttribute("forms", formsMap);
        return "indexApprover";
    }

    //请假单页面
    @RequestMapping("/form")
    public String form() {
        return "form";
    }

    @ResponseBody
    @PostMapping("/login")
    public ResultInfo login(HttpServletRequest request, HttpServletResponse response) {
        ResultInfo result = new ResultInfo();
        String username = request.getParameter("username");
        User user = miaoService.loginSuccess(username);
        if (user != null) {
            result.setCode(200);
            result.setMsg("登录成功");
            result.setInfo(user);
            //用户信息存放在Cookie中，实际情况下保存在Redis更佳
            Cookie cookie = new Cookie("userInfo", username);
            cookie.setPath("/");
            response.addCookie(cookie);
        } else {
            result.setCode(300);
            result.setMsg("登录名不存在，登录失败");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/logout")
    public ResultInfo logout(HttpServletRequest request, HttpServletResponse response) {
        ResultInfo result = new ResultInfo();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userInfo")) {
                    cookie.setValue(null);
                    // 立即销毁cookie
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        result.setCode(200);
        return result;
    }

    //添加请假单
    @ResponseBody
    @RequestMapping("/writeForm")
    public ResultInfo writeForm(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String operator) {
        ResultInfo result = new ResultInfo();
        VacationForm form = miaoService.writeForm(title, content, operator);
        result.setCode(200);
        result.setMsg("填写请假条成功");
        result.setInfo(form);
        return result;
    }

    //申请者放弃请假
    @ResponseBody
    @RequestMapping("/giveup")
    public ResultInfo giveup(HttpServletRequest request) {
        ResultInfo result = new ResultInfo();
        String formId = request.getParameter("formId");
        String operator = request.getParameter("operator");
        miaoService.completeProcess(formId, operator, "giveup");
        result.setCode(200);
        result.setMsg("放弃请假成功");
        return result;
    }

    //申请者申请请假
    @RequestMapping("/apply")
    @ResponseBody
    public ResultInfo apply(HttpServletRequest request) {
        ResultInfo result = new ResultInfo();
        String formId = request.getParameter("formId");
        String operator = request.getParameter("operator");
        miaoService.completeProcess(formId, operator, "apply");
        result.setCode(200);
        result.setMsg("申请请假成功");
        return result;
    }

    //审批者审核请假信息
    @ResponseBody
    @RequestMapping("/approve")
    public ResultInfo approve(HttpServletRequest request) {
        ResultInfo result = new ResultInfo();
        String formId = request.getParameter("formId");
        String operator = request.getParameter("operator");
        miaoService.approverVacation(formId, operator);
        result.setCode(200);
        result.setMsg("请假审核成功");
        return result;
    }

    //获取某条请假信息当前状态
    @ResponseBody
    @RequestMapping("/currentState")
    public HashMap<String, String> currentState(HttpServletRequest request) {
        String formId = request.getParameter("formId");
        HashMap<String, String> map = new HashMap<String, String>();
        map = miaoService.getCurrentState(formId);
        return map;
    }

    @ResponseBody
    @RequestMapping("/historyState")
    public ResultInfo queryHistoricTask(HttpServletRequest request) {
        ResultInfo result = new ResultInfo();
        String formId = request.getParameter("formId");
        List process = miaoService.historyState(formId);
        result.setCode(200);
        result.setInfo(process);
        return result;
    }
}
