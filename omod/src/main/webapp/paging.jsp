<%--
 *  Copyright 2009 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of Billing module.
 *
 *  Billing module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  Billing module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Billing module.  If not, see <http://www.gnu.org/licenses/>.
 *
--%>
<c:set var="moduleId" value="billing" />
<c:set var="baseLink" value="${pagingUtil.baseLink}" />
<c:set var="pageSize" value="${pagingUtil.pageSize}" />
<c:set var="currentPage" value="${pagingUtil.currentPage}" />
<c:set var="startPage" value="${pagingUtil.startPage}" />
<c:set var="numberOfPages" value="${pagingUtil.numberOfPages}" />
<%-- ghanshyam 12-sept-2012 Bug #357 [billing][3.2.7-SNAPSHOT] Error screen appears on clicking next page or changing page size in list of bills --%>
<c:set var="patientId" value="${pagingUtil.patientId}" />

<input type="hidden" id="baseLink" value="${baseLink}" />
<input type="hidden" id="currentPage" value="${currentPage}" />
<%-- ghanshyam 12-sept-2012 Bug #357 [billing][3.2.7-SNAPSHOT] Error screen appears on clicking next page or changing page size in list of bills --%>
<input type="hidden" id="patientId" value="${patientId}" />
<c:if test="${numberOfPages > 0 }">
	<ul class="pageSizeSelection">
		<li><span><spring:message
					code="${moduleId}.paging.totalpage" />:</span> ${numberOfPages}</li>
		<li><span><spring:message
					code="${moduleId}.paging.pagesize" />:</span> <input type="text"
			id="sizeOfPage" value="${pageSize}" style="width: 50px"
			onchange="changePageSize('${baseLink}');">
		</li>
		<li><span><spring:message
					code="${moduleId}.paging.jumptopage" />:</span> <input type="text"
			id="jumpToPage" value="${currentPage}" style="width: 50px"
			onchange="jumpPage('${baseLink}');">
		</li>
	</ul>
	<div class="paging">
		<c:choose>
			<c:when test="${currentPage > 1}">
				<c:set var="prev" value="${currentPage - 1 }" />
				<%-- ghanshyam 12-sept-2012 Bug #357 [billing][3.2.7-SNAPSHOT] Error screen appears on clicking next page or changing page size in list of bills --%>
				<a href="${baseLink}currentPage=1&pageSize=${pageSize}&patientId=${patientId}"
					class="first" title="First">&laquo;&laquo;</a>
				<a href="${baseLink}currentPage=${prev}&pageSize=${pageSize}&patientId=${patientId}"
					class="prev" title="Previous">&laquo;</a>
			</c:when>
			<c:otherwise>
				<span class="first" title="First">&laquo;&laquo;</span>
				<span class="prev" title="Previous">&laquo;</span>
			</c:otherwise>
		</c:choose>
		<c:forEach begin="0" end="4" step="1" var="i">
			<c:set var="p" value="${startPage + i }" />
			<c:if test="${p <= numberOfPages }">
				<c:if test="${i > 0}">
					<span class="seperator">|</span>
				</c:if>
				<c:choose>
					<c:when test="${p != currentPage }">
					<%-- ghanshyam 12-sept-2012 Bug #357 [billing][3.2.7-SNAPSHOT] Error screen appears on clicking next page or changing page size in list of bills --%>
						<a href="${baseLink}currentPage=${p}&pageSize=${pageSize}&patientId=${patientId}"
							class="page" title="Page $p">${p}</a>
					</c:when>
					<c:otherwise>
						<span class="page" title="Page $p">${p}</span>
					</c:otherwise>
				</c:choose>
			</c:if>
		</c:forEach>
		<c:choose>
			<c:when test="${currentPage < numberOfPages  }">
				<c:set var="next" value="${currentPage + 1  }" />
				<%-- ghanshyam 12-sept-2012 Bug #357 [billing][3.2.7-SNAPSHOT] Error screen appears on clicking next page or changing page size in list of bills --%>
				<a href="${baseLink}currentPage=${next}&pageSize=${pageSize}&patientId=${patientId}"
					class="next" title="Next">&raquo;</a>
				<a
					href="${baseLink}currentPage=${numberOfPages}&pageSize=${pageSize}&patientId=${patientId}"
					class="last" title="Last">&raquo;&raquo;</a>
			</c:when>
			<c:otherwise>
				<span class="next" title="Next">&raquo; </span>
				<span class="last" title="Last">&raquo;&raquo;</span>
			</c:otherwise>
		</c:choose>
	</div>
</c:if>
