package com.techacademy.controller;

import java.util.Set; 

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; 
import org.springframework.validation.annotation.Validated; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; 

import com.techacademy.entity.User;
import com.techacademy.service.UserService;

@Controller
//ここでuserを指定しておけば下記Mappingでuser/listを/listへ省略可。userは箱の名前
@RequestMapping("user")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    /** 一覧画面を表示 */
    @GetMapping("/list")//Mappingする（DBに見にいく）場所を括弧内で教える
    public String getList(Model model) {
        // 全件検索結果をModelに登録。（箱の名前,箱の中身・型）
        model.addAttribute("userlist", service.getUserList());
        // user/list.htmlに画面遷移
        return "user/list";
    }
    /** User登録画面を表示 */
    @GetMapping("/register")//Mappingする（DBに見にいく）場所を括弧内で教える
    public String getRegister(@ModelAttribute User user) {
        // User登録画面に遷移
        return "user/register";
    }

    /** User登録処理 */
    @PostMapping("/register")//Mappingする（DBに登録する）場所を括弧内で教える
    public String postRegister(@Validated User user, BindingResult res, Model model) { //resという箱に一旦入れる。エラーがある場合はこの箱を見るres→Model
        //もしresがエラーならば
        if(res.hasErrors()) {
        // userを返してください
        return getRegister(user);
        }
        // エラーがないならUser更新（コントローラーからサービスへ受け渡す）
        service.saveUser(user);
        // 一覧画面にリダイレクト
        return "redirect:/user/list";
    }
    /** User更新画面を表示 */
    @GetMapping("/update/{id}/") //Mappingする場所を括弧内で教える
    public String getUser(@PathVariable("id") Integer id, Model model) {
        // Modelに登録。idを入れるとUserが戻ってくる。
        model.addAttribute("user", service.getUser(id));
        // User更新画面に遷移
        return "user/update";
    }
    /** User更新処理 */
    @PostMapping("/update/{id}/")//Mappingする（DBを更新する）場所を括弧内で教える
    public String postUser(@Validated User user, BindingResult res, @PathVariable("id") Integer id, Model model) {
        if (res.hasErrors()) {
            // userという箱にuserを登録してください
            model.addAttribute("user", user);
            return "user/update";
        }
        // エラーがないならUser更新（コントローラーからサービスへ受け渡す）
        service.saveUser(user);
        // 一覧画面にリダイレクト
        return "redirect:/user/list";
    }

    /** User削除処理 */
    @PostMapping(path="/list", params="deleteRun")
    public String deleteRun(@RequestParam(name="idck") Set<Integer> idck, Model model) {
        // Userを一括削除
        service.deleteUser(idck);
        // 一覧画面にリダイレクト
        return "redirect:/user/list";
    }
}