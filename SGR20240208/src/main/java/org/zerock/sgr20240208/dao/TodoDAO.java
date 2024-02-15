package org.zerock.sgr20240208.dao;

import lombok.Cleanup;
import org.zerock.sgr20240208.domain.TodoVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TodoDAO {

    public String getTime() {

        String now = null;

        try(Connection connection = ConnectionUtil.INSTANCE.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select now()");
            ResultSet resultSet = preparedStatement.executeQuery();)
        {
        resultSet.next();

        now = resultSet.getString(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    public void insert(TodoVO vo) throws Exception {

        String sql = "insert into tbl_test (name, id, password, age, gender, hobbies, travel, content) values (?, ?, ?, ?, ?, ?, ?, ?)";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, vo.getName());
        preparedStatement.setString(2, vo.getId());
        preparedStatement.setString(3, vo.getPassword());
        preparedStatement.setInt(4, vo.getAge());
        preparedStatement.setString(5, vo.getGender());
        preparedStatement.setString(6, vo.getHobbies());
        preparedStatement.setString(7, vo.getTravel());
        preparedStatement.setString(8, vo.getContent());

        preparedStatement.executeUpdate();

    }


}
