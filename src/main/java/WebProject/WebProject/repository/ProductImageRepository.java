package WebProject.WebProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import WebProject.WebProject.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage,Integer>{

	//ham giao dien xoa theo idd
	void deleteById(int id);

}
