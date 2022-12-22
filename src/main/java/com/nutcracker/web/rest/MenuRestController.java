package com.nutcracker.web.rest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nutcracker.vo.MenuListVo;
import com.nutcracker.vo.MenuNameVO;
import com.nutcracker.vo.MenuVo;
import com.nutcracker.vo.SysMenuNameVO;
import com.nutcracker.vo.SysMenuVO;
import com.thyme.common.base.ApiResponse;
import com.thyme.common.utils.SecurityUtils;
import com.thyme.common.utils.UUIDUtils;
import com.thyme.system.entity.SysMenu;
import com.thyme.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author thyme
 * @ClassName MenuRestController
 * @Description 菜单RestController
 * @Date 2019/12/24 14:50
 */
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MenuRestController {

    private final SysMenuService sysMenuService;

    @GetMapping("/getMenulist")
    public ApiResponse getMenulist(){
        //获取当前用户登录用户
        Authentication userAuthentication = SecurityUtils.getCurrentUserAuthentication();
        String name = userAuthentication.getName();
        List<MenuVo> menuVoList = sysMenuService.getMenuByUser(name);
        JSONObject data = new JSONObject(16);
        data.put("name",name);
        data.put("menuList",menuVoList);
        return ApiResponse.ofSuccess(data);
    }

    @GetMapping("/getMenuInfo")
    public ApiResponse getMenuInfo(@RequestParam("page") int page,
                                   @RequestParam("page_size") int pageSize) {
        JSONObject jsonObject = new JSONObject();

        /*page = (page -1) * pageSize;*/
        List<MenuListVo> listVoList = new LinkedList<>();
        IPage<SysMenu> firstMenu = sysMenuService.findFirstMenu(new Page(page, pageSize));
        //组装数据
        List<SysMenu> firstMenuList = firstMenu.getRecords();
        for (SysMenu sysMenu : firstMenuList) {
            List<SysMenu> secondMenu = sysMenuService.findByParentId(sysMenu.getId());
            listVoList.add(MenuListVo.builder().id(sysMenu.getId())
                    .children(secondMenu)
                    .isShow(sysMenu.getIsShow())
                    .menuCode(sysMenu.getMenuCode())
                    .menuHref(sysMenu.getMenuHref())
                    .menuIcon(sysMenu.getMenuIcon())
                    .menuLevel(sysMenu.getMenuLevel())
                    .menuName(sysMenu.getMenuName())
                    .menuWeight(sysMenu.getMenuWeight()).build());
        }
        jsonObject.put("total",firstMenu.getTotal());
        jsonObject.put("page",firstMenu.getCurrent());
        jsonObject.put("page_size",firstMenu.getSize());
        jsonObject.put("menuList",listVoList);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/deleteMenu")
    public ApiResponse deleteMenu(@RequestParam("id")String id){
        JSONObject jsonObject = new JSONObject();
        try{
            if (sysMenuService.deleteMenuById(id) > 0){
                jsonObject.put("code",200);
            }
        }catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code",500);
        }
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/updateMenu")
    @ResponseBody
    public ApiResponse updateMenu(@RequestBody SysMenuNameVO sysMenuNameVO){
        JSONObject jsonObject = new JSONObject();
        SysMenuVO sysMenu = new SysMenuVO(sysMenuNameVO.getId(),sysMenuService.getByMenuName(sysMenuNameVO.getMenuNames()),sysMenuNameVO.getMenuName(), sysMenuNameVO.getMenuCode(), sysMenuNameVO.getMenuHref(),sysMenuNameVO.getMenuIcon(),
                sysMenuNameVO.getMenuLevel(),sysMenuNameVO.getMenuWeight(),sysMenuNameVO.getIsShow(),null,null);
        try{
            if (sysMenuService.updateMenu(sysMenu) > 0){
                jsonObject.put("code",200);
            }
        }catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code",500);
        }
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/addMenu")
    @ResponseBody
    public ApiResponse addMenu(@RequestBody SysMenuNameVO sysMenuNameVO){
        JSONObject jsonObject = new JSONObject();
        SysMenu menu = sysMenuService.getByName(sysMenuNameVO.getMenuName(), sysMenuNameVO.getMenuCode(), sysMenuNameVO.getMenuHref());
        if (menu == null) {
            Authentication authentication = SecurityUtils.getCurrentUserAuthentication();
            SysMenuVO sysMenuVO = new SysMenuVO(UUIDUtils.getUUID(),sysMenuService.getByMenuName(sysMenuNameVO.getMenuNames()),sysMenuNameVO.getMenuName(), sysMenuNameVO.getMenuCode(), sysMenuNameVO.getMenuHref(),sysMenuNameVO.getMenuIcon(),
                    sysMenuNameVO.getMenuLevel(),sysMenuNameVO.getMenuWeight(),sysMenuNameVO.getIsShow(),new Date(),(String)authentication.getPrincipal());
            try{
                if (sysMenuService.addMenu(sysMenuVO) > 0){
                    jsonObject.put("code",200);
                }
            }catch (Exception e) {
                e.printStackTrace();
                jsonObject.put("code",500);
            }
        } else {
            jsonObject.put("code",501);
        }
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/getMenuLevel")
    public ApiResponse getMenuLevel(){
        JSONObject jsonObject = new JSONObject();
        List<String> menuLevel = sysMenuService.getMenuLevel();
        jsonObject.put("menuLevel",menuLevel);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/getPreviousMenu")
    public ApiResponse getPreviousMenu(@RequestParam("menuLevel")String menuLevel){
        JSONObject jsonObject = new JSONObject();
        List<MenuNameVO> menuNames = sysMenuService.getPreviousMenu(String.valueOf((Integer.parseInt(menuLevel) - 1)));
        jsonObject.put("menuNames",menuNames);
        return ApiResponse.ofSuccess(jsonObject);
    }
}
