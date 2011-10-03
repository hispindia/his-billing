	
	<c:set var="moduleId" value="billing"/>
	<c:set var="baseLink" value="${pagingUtil.baseLink}" />
	<c:set var="pageSize" value="${pagingUtil.pageSize}" />
	<c:set var="currentPage" value="${pagingUtil.currentPage}" />
	<c:set var="startPage" value="${pagingUtil.startPage}" />
	<c:set var="numberOfPages" value="${pagingUtil.numberOfPages}" />	
	
	<input type="hidden" id="baseLink" value="${baseLink}"/>
	<input type="hidden" id="currentPage" value="${currentPage}"/>
	<c:if test="${numberOfPages > 0 }">
		<ul class="pageSizeSelection">
			<li><span><spring:message code="${moduleId}.paging.totalpage"/>:</span> ${numberOfPages}</li>
		  	<li><span><spring:message code="${moduleId}.paging.pagesize"/>:</span>
			 <input type="text" id="sizeOfPage" value="${pageSize}" style="width:50px" onchange="changePageSize('${baseLink}');"></li>
			<li> <span ><spring:message code="${moduleId}.paging.jumptopage"/>:</span>
			 <input type="text" id="jumpToPage" value="${currentPage}" style="width:50px" onchange="jumpPage('${baseLink}');"></li>
		</ul>
		<div class="paging">
		<c:choose>
			<c:when test="${currentPage > 1}">
				<c:set var="prev" value="${currentPage - 1 }"/>
				<a href="${baseLink}currentPage=1&pageSize=${pageSize}" class="first" title="First">&laquo;&laquo;</a>
				<a href="${baseLink}currentPage=${prev}&pageSize=${pageSize}" class="prev" title="Previous">&laquo;</a>
			</c:when>
			<c:otherwise>
				<span class="first" title="First">&laquo;&laquo;</span>
				<span class="prev" title="Previous">&laquo;</span>
			</c:otherwise>
		</c:choose>
		<c:forEach begin="0" end="4" step="1" var="i">
			<c:set var="p" value="${startPage + i }"/>
			<c:if test="${p <= numberOfPages }">
				<c:if test="${i > 0}">
					<span class="seperator">|</span>
				</c:if>
				<c:choose>
					<c:when test="${p != currentPage }">
						<a href="${baseLink}currentPage=${p}&pageSize=${pageSize}" class="page" title="Page $p">${p}</a>
					</c:when>
					<c:otherwise>
						<span class="page" title="Page $p">${p}</span>
					</c:otherwise>
				</c:choose>
			</c:if>
		</c:forEach>
		<c:choose>
			<c:when test="${currentPage < numberOfPages  }">
				<c:set var="next" value="${currentPage + 1  }"/>
				<a href="${baseLink}currentPage=${next}&pageSize=${pageSize}" class="next" title="Next">&raquo;</a>
				<a href="${baseLink}currentPage=${numberOfPages}&pageSize=${pageSize}" class="last" title="Last">&raquo;&raquo;</a>
			</c:when>
			<c:otherwise>
				<span class="next" title="Next">&raquo; </span>
				<span class="last" title="Last">&raquo;&raquo;</span>
			</c:otherwise>
		</c:choose>
		</div>
	</c:if>
