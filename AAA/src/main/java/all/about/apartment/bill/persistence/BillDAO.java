package all.about.apartment.bill.persistence;

import java.util.List;

import all.about.apartment.bill.domain.Personal_mgmt_exVO;
import all.about.apartment.bill.domain.SetMonthBill;



public interface BillDAO {
	public List<Personal_mgmt_exVO> getPersonal_mgmt_exList() throws Exception;
	public Personal_mgmt_exVO monthBill(SetMonthBill setMonthBill) throws Exception;

}
