package api.championship_management.dao.operations;

import api.championship_management.model.Season;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeasonCrudOperations {
    @Override
    public List<Season> findAll(Pagination pagination, Order order){
        StringBuilder query = new StringBuilder("select * from \"ingredient\"");
        query.append(" order by ").append(order.getOrderBy()).append(" ").append(order.getOrderValue());
        query.append(" limit ? offset ?");

        List<Ingredient> ingredients = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
            preparedStatement.setInt(1, pagination.getPageSize());
            preparedStatement.setInt(2, (pagination.getPage() - 1) * pagination.getPageSize());
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                ingredients.add(resultSetToIngredient(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredients;
    }

}
