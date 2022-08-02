package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.RespData;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName EmployeeController.java
 * @Description TODO
 * @createTime 2022年01月21日 20:13:00
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public RespData<Employee> islogin(HttpServletRequest request, @RequestBody Employee employee) {
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(lambdaQueryWrapper);
        if (emp == null) {
            return RespData.error("登录失败");
        }
        if (!emp.getPassword().equals(password)) {
            return RespData.error("登录失败");
        }
        if (emp.getStatus() == 0) {
            return RespData.error("账号已禁用");
        }
        request.getSession().setAttribute("employee", emp.getId());
        return RespData.success(emp);
    }

    @PostMapping("/logout")
    public RespData<String> islogout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return RespData.success("退出成功!");
    }

    @PostMapping
    public RespData<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工，员工信息：{}", employee.toString());
        String passowrd = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(passowrd);
        boolean save = employeeService.save(employee);
        if (save) {
            return RespData.success("新增员工成功");
        }
        return RespData.success("新增员工失败");
    }

    @GetMapping("/page")
    public RespData<Page> page(Integer page, Integer pageSize, String name) {
        log.info("page = {},pageSize = {},name = {}", page, pageSize, name);
        //设置分页构造器
        Page info = new Page(page, pageSize);
        //构造器条件设置器
        LambdaQueryWrapper<Employee> lam = new LambdaQueryWrapper();
        lam.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        lam.orderByDesc(Employee::getUpdateTime);
        employeeService.page(info, lam);
        return RespData.success(info);
    }

    @PutMapping
    public RespData<String> updete(@RequestBody Employee employee, HttpServletRequest request) {
        Long employeeInfo = (Long) request.getSession().getAttribute("employee");
        Long id = employee.getId();
        Integer status = employee.getStatus();

        //权限判断自己不禁用自己
        if (employeeInfo == id) {
            if (status == 0) {
                return RespData.error(" 权限不足");
            }
        }
        //权限判断不能禁用总权限
        if (id == 1) {
            if (status == 0) {
                return RespData.error(" 权限不足");
            }
        }
        //总权限不能让低权更改
        if (employeeInfo != 1) {
            if (id == 1) {
                return RespData.error(" 权限不足");
            }
        }

        employeeService.updateById(employee);

        return RespData.success("修改成功！");
    }

    @GetMapping("/{id}")
    public RespData<Employee> getById(@PathVariable Long id) {
        log.info("根据Id查询员工信息");
        Employee employee = employeeService.getById(id);
        return RespData.success(employee);
    }

}
