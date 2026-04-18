package dao;
import model.Promotion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static util.DBContext.getConnection;

public class PromotionDAO {
    private Connection conn;

    public PromotionDAO(Connection conn) {
        this.conn = conn;
    }
    //Lấy toàn bộ danh sách chương trình khuyến mãi
    public List<Promotion> getAllPromotion (){
        List<Promotion> list = new ArrayList<>();
        String sql=" SELECT * FROM promotions";
        try(
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs= ps.executeQuery())   {
            while(rs.next()){
                Promotion promotion= new Promotion();
                promotion.setId(rs.getInt("id"));
                promotion.setName(rs.getString("name"));
                promotion.setType(rs.getString("type"));
                promotion.setDiscount_type((rs.getString("discount_type")));
                promotion.setDiscount_value(rs.getInt("discount_value"));
                promotion.setStart_date(rs.getTimestamp("start_date"));
                promotion.setEnd_date(rs.getTimestamp("end_date"));
                promotion.setStatus(rs.getString("status"));
                list.add(promotion);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    //Thêm thoong tin vào promotion
    public void insertPromotion(Promotion promotion) throws SQLException {
        String sql = """ 
               INSERT INTO promotion 
               (name, type, discount_type, discount_value, start_date, end_date, status)
               VALUES (?,?,?,?,?,?,?)
               """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,promotion.getName());
            ps.setString(2,promotion.getType());
            ps.setString(3,promotion.getDiscount_type());
            ps.setInt(4,promotion.getDiscount_value());
            ps.setTimestamp(5,promotion.getStart_date());
            ps.setTimestamp(6,promotion.getEnd_date());
            ps.setString(7,promotion.getStatus());
            ps.executeUpdate();

           try( ResultSet rs = ps.getGeneratedKeys()){
            if (rs.next()) {
               promotion.setId( rs.getInt(1));
            }}catch (Exception e){
               e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
