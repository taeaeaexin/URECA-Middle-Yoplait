import React from 'react';
import AdminLayout from '../components/AdminLayout';

function Dashboard() {
    return (
        <AdminLayout>
            <h1>대시보드</h1>
            <iframe
                src="http://localhost:5601/app/dashboards#/view/03acf7b8-9d04-43e5-a709-9f2362c69d87?embed=true&_g=(refreshInterval:(pause:!t,value:60000),time:(from:now-15w,to:now))&_a=()&show-top-menu=false&show-query-input=false&show-time-filter=true"
                width="100%"
                height="3000px"
                style={{
                    border: "none",
                    margin: 0,
                    padding: 0,
                    display: "block"
                }}
            ></iframe>
        </AdminLayout>
    );
}


export default Dashboard;