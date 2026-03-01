using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace SUB1
{
    public partial class Form1: Form
    {
        private SqlConnection _conn;
        private SqlDataAdapter _daCabinets, _daDentists;
        private DataSet _ds;
        private BindingSource _bsCabinets, _bsDentists;
        public Form1()
        {
            InitializeComponent();
            InitializeData();
        }
        private void InitializeData()
        {
            _conn = new SqlConnection("Data Source=TEO;Initial Catalog=Dental;Integrated Security=True;");

            // Master: DentalCabinet
            _daCabinets = new SqlDataAdapter(
                "SELECT CabinetID, Name, PhoneNumber, Website FROM DentalCabinet",
                _conn
            );

            // Detail: Dentist
            _daDentists = new SqlDataAdapter(
                "SELECT DentistID, CabinetID, Name, Specialty FROM Dentist",
                _conn
            );
            var cmdBuilder = new SqlCommandBuilder(_daDentists);

            // Fill DataSet
            _ds = new DataSet();
            _daCabinets.Fill(_ds, "DentalCabinet");
            _daDentists.Fill(_ds, "Dentist");

            // Define the relation
            _ds.Relations.Add(
                "Cabinet_Dentists",
                _ds.Tables["DentalCabinet"].Columns["CabinetID"],
                _ds.Tables["Dentist"].Columns["CabinetID"]
            );

            // BindingSources
            _bsCabinets = new BindingSource(_ds, "DentalCabinet");
            _bsDentists = new BindingSource(_bsCabinets, "Cabinet_Dentists");

            // Hook up DataGridViews
            dgvDentalCabinet.DataSource = _bsCabinets;
            dgvDentists.DataSource = _bsDentists;
        }

        private void btnSave_Click_Click(object sender, EventArgs e)
        {
            // Push changes in Dentists table back to DB
            _daDentists.Update(_ds, "Dentist");
            MessageBox.Show("Dentist changes saved.");

        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }
    }
}
