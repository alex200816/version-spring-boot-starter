package cn.alex.sample.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Alex
 * @date 2024/8/26 21:04
 */
@Data
@AllArgsConstructor
public class SysUser {

    private String userName;
    private String passWord;

}
