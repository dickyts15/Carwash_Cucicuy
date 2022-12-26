/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package carwash.repository;

/**
 *
 * @author ASUS
 */
public interface AdminRepository<T, ID> {

    T login(String username, String password);
}
