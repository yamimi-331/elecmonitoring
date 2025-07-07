<footer>
    <hr>
    <div class="footer-content">
        <p>&copy; 2025 EcoTeam Inc. All rights reserved.</p>
    </div>
    <c:if test="${not empty message}">
        <script>
            alert('${message}');
        </script>
    </c:if>
</footer>

<style>
    footer {
        text-align: center;
        padding: 20px;
        color: #777;
        font-size: 0.9em;
    }
    .footer-content {
        margin-top: 10px;
    }
</style>
